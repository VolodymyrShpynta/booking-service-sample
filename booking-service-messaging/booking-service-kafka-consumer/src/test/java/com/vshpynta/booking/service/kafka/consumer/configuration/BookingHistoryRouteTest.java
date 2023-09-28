package com.vshpynta.booking.service.kafka.consumer.configuration;

import com.vshpynta.booking.service.kafka.consumer.configuration.properties.CamelKafkaConsumerConfigurationProperties;
import com.vshpynta.booking.service.kafka.consumer.handler.BookingHistoryMessageHandler;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

import static com.vshpynta.booking.service.common.utils.JsonUtils.toJson;
import static com.vshpynta.booking.service.kafka.consumer.configuration.BookingHistoryRouteConfiguration.BOOKING_HISTORY_ENDPOINT_ID;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBookingHistoryMessage;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        classes = {
                BookingHistoryRouteConfiguration.class,
                BookingHistoryRouteTest.TestConfig.class,
                CamelAutoConfiguration.class
        }
)
class BookingHistoryRouteTest {

    private static final String DIRECT_BOOKING_HISTORY_ENDPOINT_URI = "direct:bookingHistory";

    @MockBean
    private BookingHistoryMessageHandler bookingHistoryMessageHandler;

    @Autowired
    private ProducerTemplate producerTemplate;

    @EndpointInject(DIRECT_BOOKING_HISTORY_ENDPOINT_URI)
    private Endpoint consumerEndpoint;

    @Autowired
    private CamelContext camelContext;

    @Test
    void shouldSuccessfullyRouteMessageToEventHandler() {
        //given:
        var apartmentId = generatePositiveLong();
        var requestMessage = buildApartmentBookingHistoryMessage(apartmentId, TOMORROW, TWO_WEEKS_AFTER);

        var queueNotifyBuilder = new NotifyBuilder(camelContext)
                .fromRoute(BOOKING_HISTORY_ENDPOINT_ID)
                .whenDone(1)
                .create();

        //when:
        producerTemplate.sendBodyAndHeader(consumerEndpoint, toJson(requestMessage),
                KafkaConstants.KEY, String.valueOf(apartmentId));

        //then:
        queueNotifyBuilder.matches(5, TimeUnit.SECONDS);

        //and:
        verify(bookingHistoryMessageHandler).handle(requestMessage);
        verifyNoMoreInteractions(bookingHistoryMessageHandler);
    }

    @Test
    void shouldNotCallMessageHandler_ifMessageValidationFailedWhenBookingStartDateIsNull() {
        //given:
        var apartmentId = generatePositiveLong();
        var requestMessage = buildApartmentBookingHistoryMessage(apartmentId, null, TOMORROW);

        var queueNotifyBuilder = new NotifyBuilder(camelContext)
                .fromRoute(BOOKING_HISTORY_ENDPOINT_ID)
                .whenDone(1)
                .create();

        //when:
        producerTemplate.sendBodyAndHeader(consumerEndpoint, toJson(requestMessage),
                KafkaConstants.KEY, String.valueOf(apartmentId));

        //then:
        queueNotifyBuilder.matches(5, TimeUnit.SECONDS);

        //and: handler should not be called
        verifyNoInteractions(bookingHistoryMessageHandler);
    }

    @TestConfiguration
    @EnableAutoConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public CamelKafkaConsumerConfigurationProperties bookingHistoryConsumerConfiguration() {
            return new CamelKafkaConsumerConfigurationProperties() {
                @Override
                public String getUri() {
                    return DIRECT_BOOKING_HISTORY_ENDPOINT_URI;
                }
            };
        }
    }
}
