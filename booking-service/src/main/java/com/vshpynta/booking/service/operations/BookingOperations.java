package com.vshpynta.booking.service.operations;

import com.vshpynta.booking.service.rest.dto.ApartmentBooking;

import java.util.List;

public interface BookingOperations {

    ApartmentBooking bookApartment(ApartmentBooking apartmentBooking);
    List<ApartmentBooking> getApartmentsBookings();
}
