package com.vshpynta.booking.service.operations.impl;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.common.utils.CollectionsUtils;
import com.vshpynta.booking.service.exception.BookingServiceException;
import com.vshpynta.booking.service.operations.BookingOperations;
import com.vshpynta.booking.service.operations.mapper.ApartmentBookingMapper;
import com.vshpynta.booking.service.persistence.domain.ApartmentBookingEntity;
import com.vshpynta.booking.service.persistence.repository.ApartmentBookingRepository;
import com.vshpynta.booking.service.persistence.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.vshpynta.booking.service.common.utils.FunctionUtils.peek;
import static com.vshpynta.booking.service.common.utils.StreamUtils.streamOfItems;
import static com.vshpynta.booking.service.persistence.repository.specification.BookingSpecifications.overlappingBookingSpec;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ExistingRowLockBookingOperations implements BookingOperations {

    private final ApartmentBookingMapper apartmentBookingMapper;
    private final ApartmentBookingRepository apartmentBookingRepository;
    private final ApartmentRepository apartmentRepository;

    @Override
    @Transactional
    public ApartmentBooking bookApartment(ApartmentBooking apartmentBooking) {
        return Optional.ofNullable(apartmentBooking)
                .map(apartmentBookingMapper::map)
                .map(peek(this::lookApartment))
                .filter(this::hasNoOverlappingWithExistingBooking)
//                .map(peek(this::simulateLongRunningTask)) //do this simulation to have ability easily get race conditions
                .map(apartmentBookingRepository::save)
                .map(apartmentBookingMapper::map)
                //TODO: throw conflict exception and return specific error code to the client
                .orElseThrow(() -> new BookingServiceException(format("Error book apartment for request: %s. This apartment already has a booking with overlapped dates",
                        apartmentBooking)));
    }

    @Override
    public List<ApartmentBooking> getApartmentsBookings() {
        return streamOfItems(apartmentBookingRepository.findAll())
                .map(apartmentBookingMapper::map)
                .collect(toList());
    }

    @SneakyThrows
    private void simulateLongRunningTask(ApartmentBookingEntity booking) {
        TimeUnit.SECONDS.sleep(5);
    }

    private void lookApartment(ApartmentBookingEntity booking) {
        apartmentRepository.findAndLock(booking.getApartmentId())
                .orElseThrow(() -> new BookingServiceException(format("Can't find apartment by ID=%s",
                        booking.getApartmentId())));
    }

    private boolean hasNoOverlappingWithExistingBooking(ApartmentBookingEntity apartmentBooking) {
        return Optional.ofNullable(apartmentBooking)
                .map(booking -> apartmentBookingRepository.findAll(overlappingBookingSpec(booking)))
                .filter(CollectionsUtils::isNotEmpty)
                .isEmpty();
    }
}
