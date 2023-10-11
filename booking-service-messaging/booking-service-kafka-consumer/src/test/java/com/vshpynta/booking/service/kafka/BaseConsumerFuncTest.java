package com.vshpynta.booking.service.kafka;

import com.playtika.test.common.spring.DockerPresenceBootstrapConfiguration;
import com.playtika.test.kafka.configuration.EmbeddedKafkaBootstrapConfiguration;
import com.vshpynta.booking.service.kafka.consumer.configuration.BookingHistoryRouteConfiguration;
import com.vshpynta.booking.service.kafka.consumer.handler.BookingHistoryMessageHandler;
import com.vshpynta.booking.service.kafka.utils.TopicProducerUtils;
import com.vshpynta.booking.service.testing.kafka.properties.KafkaConfigurationProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;

@SpringBootTest(
        classes = {
                DockerPresenceBootstrapConfiguration.class,
                EmbeddedKafkaBootstrapConfiguration.class,
                BookingHistoryRouteConfiguration.class,
                BaseConsumerFuncTest.TestConsumerConfiguration.class
        }
)
public abstract class BaseConsumerFuncTest {

    @Autowired
    protected BookingHistoryMessageHandler bookingHistoryMessageHandlerSpy;
    @Autowired
    protected TopicProducerUtils topicProducerUtils;
    @Value("${booking.camel.kafka.consumer.booking-history.topicName}")
    protected String bookingHistoryTopic;

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigurationProperties(KafkaConfigurationProperties.class)
    public static class TestConsumerConfiguration {

        @Bean
        public TopicProducerUtils topicProducerUtils(
                Producer<String, String> testKafkaProducer) {
            return new TopicProducerUtils(testKafkaProducer);
        }

        @Bean(destroyMethod = "close")
        public Producer<String, String> testKafkaProducer(KafkaConfigurationProperties configurationProperties) {
            var producer = configurationProperties.getProducer();

            var config = new HashMap<String, Object>();
            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.getBrokers());
            config.put(ProducerConfig.CLIENT_ID_CONFIG, producer.getClientId());
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producer.getKeySerializer());
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producer.getValueSerializer());
            config.put(ProducerConfig.ACKS_CONFIG, producer.getAcks());
            config.putAll(producer.getAdditionalConfigs());
            return new KafkaProducer<>(config);
        }

        @Primary
        @Bean
        public BookingHistoryMessageHandler bookingHistoryMessageHandlerSpy(
                BookingHistoryMessageHandler bookingHistoryMessageHandler) {
            return Mockito.spy(bookingHistoryMessageHandler);
        }
    }
}
