package com.vshpynta.booking.service.client.configuration;

import com.vshpynta.booking.service.client.external.BookingPublicClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@AutoConfigureAfter(FeignAutoConfiguration.class)
@ConditionalOnProperty(prefix = "service.booking.client.public", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingBean(BookingPublicClient.class)
@PropertySource("classpath:booking-service-client.properties")
@EnableFeignClients(clients = {BookingPublicClient.class})
public class BookingPublicFeignClientAutoConfiguration {
}
