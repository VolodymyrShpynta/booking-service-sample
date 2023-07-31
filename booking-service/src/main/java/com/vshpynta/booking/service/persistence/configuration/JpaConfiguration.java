package com.vshpynta.booking.service.persistence.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.vshpynta.booking.service.persistence.repository")
@EntityScan(basePackages = "com.vshpynta.booking.service.persistence.domain")
public class JpaConfiguration {
}
