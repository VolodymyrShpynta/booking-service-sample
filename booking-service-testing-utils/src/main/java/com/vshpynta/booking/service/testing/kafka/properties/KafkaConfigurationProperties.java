package com.vshpynta.booking.service.testing.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties("kafka.test")
public class KafkaConfigurationProperties {

    private ProducerSettings producer;
    private ConsumerSettings consumer;

    @Data
    public static class ProducerSettings {
        private String brokers;
        private String clientId = "KafkaTestProducer";
        private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
        private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
        private String acks = "all";
        private Map<String, String> additionalConfigs = new HashMap<>();
    }

    @Data
    public static class ConsumerSettings {
        private String brokers;
        private String groupId = "TestConsumerGroup";
        private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
        private String autoResetOffset = "earliest";
        private Integer autoCommitIntervalMs = 100;
        private Integer metadataMaxAgeMs = 1000;
        private Map<String, String> additionalConfigs = new HashMap<>();
    }
}
