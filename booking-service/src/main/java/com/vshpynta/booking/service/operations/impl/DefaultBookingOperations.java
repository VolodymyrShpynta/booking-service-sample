package com.vshpynta.booking.service.operations.impl;

import com.vshpynta.booking.service.exception.BookingServiceException;
import com.vshpynta.booking.service.operations.BookingOperations;
import com.vshpynta.booking.service.operations.mapper.ApartmentBookingMapper;
import com.vshpynta.booking.service.persistence.domain.ApartmentBookingEntity;
import com.vshpynta.booking.service.persistence.repository.ApartmentBookingRepository;
import com.vshpynta.booking.service.rest.dto.ApartmentBooking;
import com.vshpynta.booking.service.utils.CollectionsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.vshpynta.booking.service.persistence.repository.specification.BookingSpecifications.overlappingBookingSpec;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DefaultBookingOperations implements BookingOperations {

    private final ApartmentBookingMapper apartmentBookingMapper;
    private final ApartmentBookingRepository apartmentBookingRepository;

    @Override
    @Transactional
    public ApartmentBooking bookApartment(ApartmentBooking apartmentBooking) {
        return Optional.ofNullable(apartmentBooking)
                .map(apartmentBookingMapper::map)
                .filter(this::hasNoOverlappingWithExistingBooking)
                .map(apartmentBookingRepository::save)
                .map(apartmentBookingMapper::map)
                //TODO: throw conflict exception and return specific error code to the client
                .orElseThrow(() -> new BookingServiceException(format("Error book apartment for request: %s",
                        apartmentBooking)));
    }

    private boolean hasNoOverlappingWithExistingBooking(ApartmentBookingEntity apartmentBooking) {
        return Optional.ofNullable(apartmentBooking)
                .map(booking -> apartmentBookingRepository.findAll(overlappingBookingSpec(booking)))
                .filter(CollectionsUtils::isNotEmpty)
                .isEmpty();
    }
}
