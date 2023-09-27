package com.vshpynta.booking.service.kafka.consumer.configuration;

import com.vshpynta.booking.service.common.model.messaging.ApartmentBookingHistoryMessage;
import com.vshpynta.booking.service.kafka.consumer.configuration.properties.CamelKafkaConsumerConfigurationProperties;
import com.vshpynta.booking.service.kafka.consumer.handler.BookingHistoryMessageHandler;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.vshpynta.booking.service.kafka.consumer.configuration.JacksonDataFormatFactory.createJacksonDataFormat;
import static org.apache.camel.LoggingLevel.INFO;

@Configuration(proxyBeanMethods = false)
public class BookingHistoryRouteConfiguration {

    public static final String BOOKING_HISTORY_ENDPOINT_ID = "bookingHistory";
    private static final String BOOKING_HISTORY_EXCEPTIONS_METRIC = "counter.messages.bookingHistory.exception.count";

    @Bean
    @ConfigurationProperties("booking.camel.kafka.consumer.booking-history")
    public CamelKafkaConsumerConfigurationProperties bookingHistoryConsumerConfiguration() {
        return new CamelKafkaConsumerConfigurationProperties();
    }

    @Bean
    public RouteBuilder bookingHistoryRoute(CamelKafkaConsumerConfigurationProperties bookingHistoryConsumerConfiguration,
                                            BookingHistoryMessageHandler bookingHistoryMessageHandler) {
        return new AbstractErrorHandlingAwareRouteBuilder(BOOKING_HISTORY_EXCEPTIONS_METRIC) {

            @Override
            public void setupRoutes() {
                from(bookingHistoryConsumerConfiguration.getUri())
                        .routeId(BOOKING_HISTORY_ENDPOINT_ID)
                        .unmarshal(createJacksonDataFormat(ApartmentBookingHistoryMessage.class))
//                        .to("bean-validator://x") //TODO: implement custom validator
                        .log(INFO, "Received booking history message: ${body}")
                        .bean(bookingHistoryMessageHandler);
            }
        };
    }

    @Bean
    public BookingHistoryMessageHandler bookingHistoryMessageHandler() {
        return new BookingHistoryMessageHandler();
    }
}
