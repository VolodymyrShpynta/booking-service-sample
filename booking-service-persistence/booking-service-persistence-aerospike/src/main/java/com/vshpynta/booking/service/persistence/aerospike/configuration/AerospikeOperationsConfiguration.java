package com.vshpynta.booking.service.persistence.aerospike.configuration;

import com.vshpynta.booking.service.common.service.TimeService;
import com.vshpynta.booking.service.persistence.aerospike.ApartmentBookingOperations;
import com.vshpynta.booking.service.persistence.aerospike.repository.ApartmentBookingsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AerospikeOperationsConfiguration {

    @Bean
    public ApartmentBookingOperations apartmentBookingOperations(ApartmentBookingsRepository repository,
                                                                 TimeService timeService) {
        return new ApartmentBookingOperations(repository, timeService);
    }
}
