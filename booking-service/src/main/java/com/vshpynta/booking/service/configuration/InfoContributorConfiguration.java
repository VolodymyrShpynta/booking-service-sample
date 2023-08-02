package com.vshpynta.booking.service.configuration;

import com.vshpynta.booking.service.configuration.management.info.SwaggerInfoContributor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfoContributorConfiguration {

    @Bean
    public SwaggerInfoContributor swaggerInfoContributor(
            @Value("${spring.cloud.client.ip-address:localhost}") String host,
            @Value("${server.servlet.context-path}") String contextPath,
            @Value("${server.port}") int serverPort) {
        return new SwaggerInfoContributor(host, serverPort, contextPath);
    }
}
