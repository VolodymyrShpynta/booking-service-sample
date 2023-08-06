package com.vshpynta.booking.service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private static final String APARTMENTS_PUBLIC_API_SWAGGER_GROUP_NAME = "apartments-public-api";
    private static final String APARTMENT_BOOKING_PUBLIC_API_SWAGGER_GROUP_NAME = "booking-public-api";

    private final Optional<BuildProperties> buildProperties;

    @Bean
    public GroupedOpenApi apartmentsPublicApi() {
        return GroupedOpenApi.builder()
                .group(APARTMENTS_PUBLIC_API_SWAGGER_GROUP_NAME)
                .pathsToMatch("/public/apartments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi bookingPublicApi() {
        return GroupedOpenApi.builder()
                .group(APARTMENT_BOOKING_PUBLIC_API_SWAGGER_GROUP_NAME)
                .pathsToMatch("/public/booking/**")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .version(buildProperties.map(BuildProperties::getVersion).orElse("UNKNOWN"))
                        .title("Booking Service API"));
    }
}
