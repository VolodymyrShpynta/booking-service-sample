package com.vshpynta.booking.service.exception;

import static com.vshpynta.booking.service.rest.dto.error.PublicErrorCode.APARTMENT_NOT_FOUND;
import static java.lang.String.format;

public class ApartmentNotFoundException extends BookingServiceException {

    @Override
    public String getErrorCode() {
        return APARTMENT_NOT_FOUND.getCode();
    }

    public ApartmentNotFoundException(long apartmentId) {
        super(format("Apartment with id=%s is not found", apartmentId));
    }
}
