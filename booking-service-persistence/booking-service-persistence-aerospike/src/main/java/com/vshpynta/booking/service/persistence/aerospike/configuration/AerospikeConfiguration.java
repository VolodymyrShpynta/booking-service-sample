package com.vshpynta.booking.service.persistence.aerospike.configuration;

import com.aerospike.client.Host;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.async.EventLoops;
import com.aerospike.client.async.NioEventLoops;
import com.aerospike.client.policy.ClientPolicy;
import com.vshpynta.booking.service.persistence.aerospike.configuration.properties.AerospikeConfigurationProperties;
import com.vshpynta.booking.service.persistence.aerospike.converter.CustomSpringDataConverters.InstantToLongConverter;
import com.vshpynta.booking.service.persistence.aerospike.converter.CustomSpringDataConverters.LongToInstantConverter;
import com.vshpynta.booking.service.persistence.aerospike.repository.ApartmentBookingsRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractReactiveAerospikeDataConfiguration;
import org.springframework.data.aerospike.convert.MappingAerospikeConverter;
import org.springframework.data.aerospike.core.AerospikeExceptionTranslator;
import org.springframework.data.aerospike.core.AerospikeTemplate;
import org.springframework.data.aerospike.mapping.AerospikeMappingContext;
import org.springframework.data.aerospike.query.FilterExpressionsBuilder;
import org.springframework.data.aerospike.query.QueryEngine;
import org.springframework.data.aerospike.query.StatementBuilder;
import org.springframework.data.aerospike.query.cache.IndexInfoParser;
import org.springframework.data.aerospike.query.cache.IndexRefresher;
import org.springframework.data.aerospike.query.cache.IndexesCacheUpdater;
import org.springframework.data.aerospike.query.cache.InternalIndexOperations;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableRetry
@EnableConfigurationProperties
@EnableAerospikeRepositories(basePackageClasses = ApartmentBookingsRepository.class)
public class AerospikeConfiguration extends AbstractReactiveAerospikeDataConfiguration {

    @Override
    protected EventLoops eventLoops() {
        return new NioEventLoops(aerospikeConfigurationProperties().getEventLoopsSize());
    }

    @Override
    protected Collection<Host> getHosts() {
        return Host.parseServiceHosts(aerospikeConfigurationProperties().getHosts());
    }

    @Override
    protected String nameSpace() {
        return aerospikeConfigurationProperties().getNamespace();
    }

    @Override
    protected List<?> customConverters() {
        return List.of(
                InstantToLongConverter.INSTANCE,
                LongToInstantConverter.INSTANCE
        );
    }

    @Override
    protected ClientPolicy getClientPolicy() {
        var properties = aerospikeConfigurationProperties();

        var clientPolicy = new ClientPolicy();
        clientPolicy.timeout = properties.getConnectTimeoutMs();
        clientPolicy.user = properties.getUser();
        clientPolicy.password = properties.getPassword();
        clientPolicy.clusterName = properties.getClusterName();
        clientPolicy.maxConnsPerNode = properties.getMaxConnectionsPerNode();
        clientPolicy.tendInterval = properties.getTendIntervalMs();

        clientPolicy.writePolicyDefault.totalTimeout = properties.getOperationTimeoutMs();
        clientPolicy.writePolicyDefault.socketTimeout = properties.getOperationTimeoutMs();
        //Disabled client retries because we use spring-retry on operations level
        clientPolicy.writePolicyDefault.maxRetries = 0;

        clientPolicy.readPolicyDefault.totalTimeout = properties.getOperationTimeoutMs();
        clientPolicy.readPolicyDefault.socketTimeout = properties.getOperationTimeoutMs();
        //Disabled client retries because we use spring-retry on operations level
        clientPolicy.readPolicyDefault.maxRetries = 0;
        clientPolicy.readPolicyDefault.sleepBetweenRetries = 0;

        clientPolicy.batchPolicyDefault.totalTimeout = properties.getOperationTimeoutMs();
        clientPolicy.batchPolicyDefault.socketTimeout = properties.getOperationTimeoutMs();
        //Disabled client retries because we use spring-retry on operations level
        clientPolicy.batchPolicyDefault.maxRetries = 0;
        clientPolicy.batchPolicyDefault.sleepBetweenRetries = 0;

        clientPolicy.eventLoops = eventLoops();

        return clientPolicy;
    }

    @Bean
    @ConfigurationProperties("aerospike")
    public AerospikeConfigurationProperties aerospikeConfigurationProperties() {
        return new AerospikeConfigurationProperties();
    }

    @Bean
    public AerospikeTemplate aerospikeTemplate(IAerospikeClient aerospikeClient,
                                               MappingAerospikeConverter mappingAerospikeConverter,
                                               AerospikeMappingContext aerospikeMappingContext,
                                               AerospikeExceptionTranslator aerospikeExceptionTranslator,
                                               QueryEngine queryEngine,
                                               IndexRefresher indexRefresher) {
        return new AerospikeTemplate(aerospikeClient,
                this.nameSpace(),
                mappingAerospikeConverter,
                aerospikeMappingContext,
                aerospikeExceptionTranslator,
                queryEngine,
                indexRefresher);
    }

    @Bean
    public QueryEngine queryEngine(IAerospikeClient aerospikeClient,
                                   StatementBuilder statementBuilder,
                                   FilterExpressionsBuilder filterExpressionsBuilder) {
        QueryEngine queryEngine = new QueryEngine(aerospikeClient,
                statementBuilder, filterExpressionsBuilder, aerospikeClient.getQueryPolicyDefault());
        queryEngine.setScansEnabled(this.aerospikeDataSettings().isScansEnabled());
        return queryEngine;
    }

    @Bean
    public IndexRefresher indexRefresher(IAerospikeClient aerospikeClient, IndexesCacheUpdater indexesCacheUpdater) {
        IndexRefresher refresher = new IndexRefresher(aerospikeClient, aerospikeClient.getInfoPolicyDefault(),
                new InternalIndexOperations(new IndexInfoParser()), indexesCacheUpdater);
        refresher.refreshIndexes();
        return refresher;
    }
}
