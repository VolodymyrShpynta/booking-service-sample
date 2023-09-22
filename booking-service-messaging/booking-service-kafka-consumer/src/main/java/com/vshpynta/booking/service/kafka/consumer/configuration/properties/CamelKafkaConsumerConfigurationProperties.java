package com.vshpynta.booking.service.kafka.consumer.configuration.properties;

import com.vshpynta.booking.service.common.utils.UriProvider;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

/**
 * When using spring-boot 1.5.1 be aware of: https://github.com/spring-cloud/spring-cloud-commons/issues/177 and
 * https://github.com/spring-projects/spring-boot/issues/8173
 * <p>
 * This class is for Kafka client 0.9+ only and is NOT backward compatible with 0.8.x client.
 * Make sure to use camel-kafka 2.17+.
 * <p>
 * How to properly integrate Camel in Spring Boot services see:
 * <a href="https://wiki.corp/pages/viewpage.action?pageId=63391055">How to integrate Camel</a>
 *
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html">Spring boot externalized configuration</a>
 * @see <a href="http://camel.apache.org/kafka.html">Camel Kafka configuration</a>
 * @see <a href="http://kafka.apache.org/documentation.html#newconsumerconfigs">Kafka consumer configuration</a>
 * @see org.apache.camel.component.kafka.KafkaConfiguration
 */
@Validated
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class CamelKafkaConsumerConfigurationProperties {

    @NotNull
    private String brokerList;
    @NotNull
    private String topicName;
    @NotNull
    private String keyDeserializer;
    @NotNull
    private String valueDeserializer;
    @NotNull
    private Boolean autoCommitEnable;
    @NotNull
    private String groupId;
    @NotNull
    private String autoOffsetReset;//"latest,earliest,none"
    @NotNull
    private Integer consumersCount;

    // optional parameters
    private Integer autoCommitIntervalMs;
    private Integer fetchMinBytes;
    private Integer fetchMaxBytes;
    private Integer fetchWaitMaxMs;
    private String clientId;
    private String interceptorClasses;
    private Integer heartbeatIntervalMs;
    private Integer maxPartitionFetchBytes;
    private Integer sessionTimeoutMs;
    private Integer maxPollRecords;
    private Long pollTimeoutMs;
    private String partitionAssignor;
    private Integer consumerRequestTimeoutMs;
    private Boolean checkCrcs;
    private String seekTo;//"beginning,end"
    private String autoCommitOnStop;//"sync,async,none"
    private Boolean breakOnFirstError;
    private Boolean allowManualCommit;
    private Long maxPollIntervalMs;

    // optional security parameters
    private String securityProtocol;
    private String sslKeyPassword;
    private String sslKeystoreLocation;
    private String sslKeystorePassword;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String sslEnabledProtocols;
    private String sslKeystoreType;
    private String sslProtocol;
    private String sslProvider;
    private String sslTruststoreType;
    private String sslCipherSuites;
    private String sslEndpointAlgorithm;
    private String sslKeymanagerAlgorithm;
    private String sslTrustmanagerAlgorithm;
    private String saslKerberosServiceName;
    private String kerberosInitCmd;
    private Integer kerberosBeforeReloginMinTime;
    private Double kerberosRenewJitter;
    private Double kerberosRenewWindowFactor;
    private String kerberosPrincipalToLocalRules;
    private String saslMechanism;
    private String saslJaasConfig;

    public String getUri() {
        StringBuilder options = new StringBuilder("kafka:");
        UriProvider.addRequiredStart(options, "topic", getTopicName());
        UriProvider.addFirstRequiredProperty(options, "brokers", getBrokerList());
        UriProvider.addRequiredProperty(options, "keyDeserializer", getKeyDeserializer());
        UriProvider.addRequiredProperty(options, "valueDeserializer", getValueDeserializer());
        UriProvider.addRequiredProperty(options, "autoCommitEnable", getAutoCommitEnable());
        UriProvider.addRequiredProperty(options, "groupId", getGroupId());
        UriProvider.addRequiredProperty(options, "autoOffsetReset", getAutoOffsetReset());
        UriProvider.addRequiredProperty(options, "consumersCount", getConsumersCount());

        UriProvider.addOptionalProperty(options, "autoCommitIntervalMs", getAutoCommitIntervalMs());
        UriProvider.addOptionalProperty(options, "fetchMinBytes", getFetchMinBytes());
        UriProvider.addOptionalProperty(options, "fetchMaxBytes", getFetchMaxBytes());
        UriProvider.addOptionalProperty(options, "fetchWaitMaxMs", getFetchWaitMaxMs());
        UriProvider.addOptionalProperty(options, "clientId", getClientId());
        UriProvider.addOptionalProperty(options, "interceptorClasses", getInterceptorClasses());
        UriProvider.addOptionalProperty(options, "heartbeatIntervalMs", getHeartbeatIntervalMs());
        UriProvider.addOptionalProperty(options, "maxPartitionFetchBytes", getMaxPartitionFetchBytes());
        UriProvider.addOptionalProperty(options, "sessionTimeoutMs", getSessionTimeoutMs());
        UriProvider.addOptionalProperty(options, "maxPollRecords", getMaxPollRecords());
        UriProvider.addOptionalProperty(options, "pollTimeoutMs", getPollTimeoutMs());
        UriProvider.addOptionalProperty(options, "partitionAssignor", getPartitionAssignor());
        UriProvider.addOptionalProperty(options, "consumerRequestTimeoutMs", getConsumerRequestTimeoutMs());
        UriProvider.addOptionalProperty(options, "checkCrcs", getCheckCrcs());
        UriProvider.addOptionalProperty(options, "seekTo", getSeekTo());
        UriProvider.addOptionalProperty(options, "autoCommitOnStop", getAutoCommitOnStop());
        UriProvider.addOptionalProperty(options, "breakOnFirstError", getBreakOnFirstError());
        UriProvider.addOptionalProperty(options, "allowManualCommit", getAllowManualCommit());
        UriProvider.addOptionalProperty(options, "maxPollIntervalMs", getMaxPollIntervalMs());

        UriProvider.addOptionalProperty(options, "securityProtocol", getSecurityProtocol());
        UriProvider.addOptionalProperty(options, "sslKeyPassword", getSslKeyPassword());
        UriProvider.addOptionalProperty(options, "sslKeystoreLocation", getSslKeystoreLocation());
        UriProvider.addOptionalProperty(options, "sslKeystorePassword", getSslKeystorePassword());
        UriProvider.addOptionalProperty(options, "sslTruststoreLocation", getSslTruststoreLocation());
        UriProvider.addOptionalProperty(options, "sslTruststorePassword", getSslTruststorePassword());
        UriProvider.addOptionalProperty(options, "sslEnabledProtocols", getSslEnabledProtocols());
        UriProvider.addOptionalProperty(options, "sslKeystoreType", getSslKeystoreType());
        UriProvider.addOptionalProperty(options, "sslProtocol", getSslProtocol());
        UriProvider.addOptionalProperty(options, "sslProvider", getSslProvider());
        UriProvider.addOptionalProperty(options, "sslTruststoreType", getSslTruststoreType());
        UriProvider.addOptionalProperty(options, "sslCipherSuites", getSslCipherSuites());
        UriProvider.addOptionalProperty(options, "sslEndpointAlgorithm", getSslEndpointAlgorithm());
        UriProvider.addOptionalProperty(options, "sslKeymanagerAlgorithm", getSslKeymanagerAlgorithm());
        UriProvider.addOptionalProperty(options, "sslTrustmanagerAlgorithm", getSslTrustmanagerAlgorithm());
        UriProvider.addOptionalProperty(options, "saslKerberosServiceName", getSaslKerberosServiceName());
        UriProvider.addOptionalProperty(options, "kerberosInitCmd", getKerberosInitCmd());
        UriProvider.addOptionalProperty(options, "kerberosBeforeReloginMinTime", getKerberosBeforeReloginMinTime());
        UriProvider.addOptionalProperty(options, "kerberosRenewJitter", getKerberosRenewJitter());
        UriProvider.addOptionalProperty(options, "kerberosRenewWindowFactor", getKerberosRenewWindowFactor());
        UriProvider.addOptionalProperty(options, "kerberosPrincipalToLocalRules", getKerberosPrincipalToLocalRules());
        UriProvider.addOptionalProperty(options, "saslMechanism", getSaslMechanism());
        UriProvider.addOptionalProperty(options, "saslJaasConfig", getSaslJaasConfig());

        return options.toString();
    }
}
