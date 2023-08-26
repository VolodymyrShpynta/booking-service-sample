package com.vshpynta.booking.service.persistence.aerospike.configuration;

import com.vshpynta.booking.service.common.service.TimeService;
import com.vshpynta.booking.service.common.service.impl.DefaultTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeConfiguration {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public TimeService timeService(Clock clock) {
        return new DefaultTimeService(clock);
    }
}
