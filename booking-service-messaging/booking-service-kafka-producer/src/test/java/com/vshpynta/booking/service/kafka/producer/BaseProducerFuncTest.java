package com.vshpynta.booking.service.kafka.producer;

import com.playtika.test.common.spring.DockerPresenceBootstrapConfiguration;
import com.playtika.test.kafka.configuration.EmbeddedKafkaBootstrapConfiguration;
import com.vshpynta.booking.service.kafka.producer.configuration.ProducersConfiguration;
import com.vshpynta.booking.service.kafka.utils.TopicConsumerUtils;
import com.vshpynta.booking.service.testing.kafka.properties.KafkaConfigurationProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@SpringBootTest(
        classes = {
                DockerPresenceBootstrapConfiguration.class,
                EmbeddedKafkaBootstrapConfiguration.class,
                ProducersConfiguration.class,
                BaseProducerFuncTest.TestConsumerConfiguration.class
        }
)
public abstract class BaseProducerFuncTest {

    @Autowired
    protected BookingHistoryEventSender bookingHistoryEventSender;
    @Autowired
    protected TopicConsumerUtils topicConsumerUtils;
    @Value("${booking.camel.kafka.producer.booking-history.topicName}")
    protected String bookingHistoryTopic;

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigurationProperties(KafkaConfigurationProperties.class)
    public static class TestConsumerConfiguration {

        @Bean(destroyMethod = "close")
        public Consumer<String, String> testKafkaConsumer(KafkaConfigurationProperties configurationProperties) {
            var consumer = configurationProperties.getConsumer();

            var config = new HashMap<String, Object>();
            config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumer.getBrokers());
            config.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.getGroupId());
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumer.getAutoResetOffset());
            config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumer.getKeyDeserializer());
            config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumer.getValueDeserializer());
            config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(consumer.getAutoCommitIntervalMs()));
            config.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, String.valueOf(consumer.getMetadataMaxAgeMs()));

            config.putAll(consumer.getAdditionalConfigs());
            return new KafkaConsumer<>(config);
        }

        @Bean
        public TopicConsumerUtils kafkaConsumerUtils(Consumer<String, String> testKafkaConsumer) {
            return new TopicConsumerUtils(testKafkaConsumer);
        }
    }
}
