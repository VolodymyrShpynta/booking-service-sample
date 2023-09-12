package com.vshpynta.booking.service.configuration;

import com.vshpynta.booking.service.kafka.producer.BookingHistoryEventSender;
import com.vshpynta.booking.service.operations.BookingOperations;
import com.vshpynta.booking.service.operations.impl.ExistingRowLockBookingOperations;
import com.vshpynta.booking.service.operations.impl.HistoryBookingOperations;
import com.vshpynta.booking.service.operations.mapper.ApartmentBookingMapper;
import com.vshpynta.booking.service.persistence.repository.ApartmentBookingRepository;
import com.vshpynta.booking.service.persistence.repository.ApartmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingOperationsConfiguration {

    @Bean
    public BookingOperations existingRowLockBookingOperations(ApartmentBookingMapper apartmentBookingMapper,
                                                              ApartmentBookingRepository apartmentBookingRepository,
                                                              ApartmentRepository apartmentRepository) {
        return new ExistingRowLockBookingOperations(apartmentBookingMapper,
                apartmentBookingRepository,
                apartmentRepository);
    }

    @Bean
    public BookingOperations bookingOperations(BookingOperations existingRowLockBookingOperations,
                                               BookingHistoryEventSender bookingHistoryEventSender) {
        return new HistoryBookingOperations(existingRowLockBookingOperations, bookingHistoryEventSender);
    }
}
