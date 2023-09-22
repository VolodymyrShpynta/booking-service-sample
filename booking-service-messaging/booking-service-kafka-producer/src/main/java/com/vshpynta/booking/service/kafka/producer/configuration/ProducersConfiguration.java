package com.vshpynta.booking.service.kafka.producer.configuration;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.kafka.producer.BookingHistoryEventSender;
import com.vshpynta.booking.service.kafka.producer.configuration.properties.CamelKafkaProducerConfigurationProperties;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.vshpynta.booking.service.kafka.producer.configuration.JacksonDataFormatFactory.createJacksonDataFormat;

@Configuration
@ImportAutoConfiguration(classes = {CamelAutoConfiguration.class})
public class ProducersConfiguration {

    public static final String BOOKING_HISTORY_EVENT_ENDPOINT = "direct:bookingHistory";
    public static final String BOOKING_HISTORY_ROUTE_ID = "bookingHistoryRouteId";

    @Bean
    public RouteBuilder bookingHistoryRoutes(
            CamelKafkaProducerConfigurationProperties bookingHistoryProducerConfiguration) {

        return new RouteBuilder() {
            @Override
            public void configure() {
                from(BOOKING_HISTORY_EVENT_ENDPOINT)
                        .log(LoggingLevel.DEBUG, "Sending booking history event to Kafka: ${body}")
                        .marshal(createJacksonDataFormat(ApartmentBooking.class))
                        .to(bookingHistoryProducerConfiguration.getUri())
                        .log(LoggingLevel.INFO, "Sent booking history event to Kafka: ${body}")
                        .id(BOOKING_HISTORY_ROUTE_ID)
                        .routeId(BOOKING_HISTORY_ROUTE_ID);
            }
        };
    }

    @Bean
    @ConfigurationProperties("booking.camel.kafka.producer.booking-history")
    public CamelKafkaProducerConfigurationProperties bookingHistoryProducerConfiguration() {
        return new CamelKafkaProducerConfigurationProperties();
    }

    @Bean
    public BookingHistoryEventSender bookingHistoryEventSender(ProducerTemplate producerTemplate) {
        return new BookingHistoryEventSender(BOOKING_HISTORY_EVENT_ENDPOINT, producerTemplate);
    }
}
