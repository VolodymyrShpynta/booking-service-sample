package com.vshpynta.booking.service.operations.impl;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.exception.BookingServiceException;
import com.vshpynta.booking.service.kafka.producer.BookingHistoryEventSender;
import com.vshpynta.booking.service.operations.BookingOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.vshpynta.booking.service.common.utils.FunctionUtils.peek;
import static java.lang.String.format;

@RequiredArgsConstructor
public class HistoryBookingOperations implements BookingOperations {

    private final BookingOperations bookingOperations;
    private final BookingHistoryEventSender bookingHistoryEventSender;

    @Override
    @Transactional
    public ApartmentBooking bookApartment(ApartmentBooking apartmentBooking) {
        return Optional.ofNullable(apartmentBooking)
                .map(bookingOperations::bookApartment)
                .map(peek(this::sendHistoryMessage))
                .orElseThrow(() -> new BookingServiceException(format("Error book apartment for request: %s.",
                        apartmentBooking)));
    }

    @Override
    public List<ApartmentBooking> getApartmentsBookings() {
        return bookingOperations.getApartmentsBookings();
    }

    private void sendHistoryMessage(ApartmentBooking booking) {
        bookingHistoryEventSender.sendEvent(booking);
    }
}
