package com.vshpynta.booking.service.operations.impl;

import com.vshpynta.booking.service.exception.BookingServiceException;
import com.vshpynta.booking.service.operations.BookingOperations;
import com.vshpynta.booking.service.operations.mapper.ApartmentBookingMapper;
import com.vshpynta.booking.service.persistence.repository.ApartmentBookingRepository;
import com.vshpynta.booking.service.rest.dto.ApartmentBooking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DefaultBookingOperations implements BookingOperations {

    private final ApartmentBookingMapper apartmentBookingMapper;
    private final ApartmentBookingRepository apartmentBookingRepository;

    @Override //TODO: implement booking overlapping verification
    @Transactional
    public ApartmentBooking bookApartment(ApartmentBooking apartmentBooking) {
        return Optional.ofNullable(apartmentBooking)
                .map(apartmentBookingMapper::map)
                .map(apartmentBookingRepository::save)
                .map(apartmentBookingMapper::map)
                .orElseThrow(() -> new BookingServiceException(format("Error book apartment for request: %s",
                        apartmentBooking)));
    }
}
