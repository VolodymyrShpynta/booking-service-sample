package com.vshpynta.booking.service.kafka.producer.configuration.properties;

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
 *
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html">Spring boot externalized configuration</a>
 * @see <a href="http://camel.apache.org/kafka.html">Camel Kafka configuration</a>
 * @see <a href="http://kafka.apache.org/documentation.html#producerconfigs">Kafka producer configuration</a>
 * @see org.apache.camel.component.kafka.KafkaConfiguration
 */
@Slf4j
@Validated
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CamelKafkaProducerConfigurationProperties implements UriProvider {
    @NotNull
    private String brokerList;
    @NotNull
    private String topicName;
    @NotNull
    private String valueSerializer;
    @NotNull
    private String keySerializer;

    // optional parameters
    @Builder.Default
    private String requestRequiredAcks = "0";
    private String partitioner;
    private Integer requestTimeoutMs;
    private String compressionCodec;
    private Integer retryBackoffMs;
    private Integer queueBufferingMaxMessages;
    private Integer sendBufferBytes;
    private String clientId;
    private String interceptorClasses;
    private Boolean bridgeEndpoint;
    private Boolean circularTopicDetection;
    private Integer workerPoolCoreSize;
    private Integer workerPoolMaxSize;
    private Integer bufferMemorySize;
    private Integer retries;
    private Integer producerBatchSize;
    private Integer connectionMaxIdleMs;
    private Integer lingerMs;
    private Integer maxBlockMs;
    private Integer maxRequestSize;
    private Integer receiveBufferBytes;
    private String key;
    private Integer partitionKey;
    private Boolean recordMetadata;
    private Integer maxInFlightRequest;
    private Integer metadataMaxAgeMs;
    private String metricReporters;
    private Integer noOfMetricsSample;
    private Integer metricsSampleWindowMs;
    private Integer reconnectBackoffMs;
    private Boolean synchronousEndpoint;

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

    @Override
    public String getUri() {
        var options = new StringBuilder("kafka:");
        UriProvider.addRequiredStart(options, "topic", getTopicName());
        UriProvider.addFirstRequiredProperty(options, "brokers", getBrokerList());
        UriProvider.addRequiredProperty(options, "valueSerializer", getValueSerializer());
        UriProvider.addRequiredProperty(options, "keySerializer", getKeySerializer());

        UriProvider.addOptionalProperty(options, "requestRequiredAcks", getRequestRequiredAcks());
        UriProvider.addOptionalProperty(options, "partitioner", getPartitioner());
        UriProvider.addOptionalProperty(options, "requestTimeoutMs", getRequestTimeoutMs());
        UriProvider.addOptionalProperty(options, "compressionCodec", getCompressionCodec());
        UriProvider.addOptionalProperty(options, "retryBackoffMs", getRetryBackoffMs());
        UriProvider.addOptionalProperty(options, "queueBufferingMaxMessages", getQueueBufferingMaxMessages());
        UriProvider.addOptionalProperty(options, "sendBufferBytes", getSendBufferBytes());
        UriProvider.addOptionalProperty(options, "clientId", getClientId());
        UriProvider.addOptionalProperty(options, "interceptorClasses", getInterceptorClasses());
        UriProvider.addOptionalProperty(options, "bridgeEndpoint", getBridgeEndpoint());
        UriProvider.addOptionalProperty(options, "circularTopicDetection", getCircularTopicDetection());
        UriProvider.addOptionalProperty(options, "workerPoolCoreSize", getWorkerPoolCoreSize());
        UriProvider.addOptionalProperty(options, "workerPoolMaxSize", getWorkerPoolMaxSize());
        UriProvider.addOptionalProperty(options, "bufferMemorySize", getBufferMemorySize());
        UriProvider.addOptionalProperty(options, "retries", getRetries());
        UriProvider.addOptionalProperty(options, "producerBatchSize", getProducerBatchSize());
        UriProvider.addOptionalProperty(options, "connectionMaxIdleMs", getConnectionMaxIdleMs());
        UriProvider.addOptionalProperty(options, "lingerMs", getLingerMs());
        UriProvider.addOptionalProperty(options, "maxBlockMs", getMaxBlockMs());
        UriProvider.addOptionalProperty(options, "maxRequestSize", getMaxRequestSize());
        UriProvider.addOptionalProperty(options, "receiveBufferBytes", getReceiveBufferBytes());
        UriProvider.addOptionalProperty(options, "key", getKey());
        UriProvider.addOptionalProperty(options, "partitionKey", getPartitionKey());
        UriProvider.addOptionalProperty(options, "recordMetadata", getRecordMetadata());
        UriProvider.addOptionalProperty(options, "maxInFlightRequest", getMaxInFlightRequest());
        UriProvider.addOptionalProperty(options, "metadataMaxAgeMs", getMetadataMaxAgeMs());
        UriProvider.addOptionalProperty(options, "metricReporters", getMetricReporters());
        UriProvider.addOptionalProperty(options, "noOfMetricsSample", getNoOfMetricsSample());
        UriProvider.addOptionalProperty(options, "metricsSampleWindowMs", getMetricsSampleWindowMs());
        UriProvider.addOptionalProperty(options, "reconnectBackoffMs", getReconnectBackoffMs());
        UriProvider.addOptionalProperty(options, "synchronous", getSynchronousEndpoint());

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
