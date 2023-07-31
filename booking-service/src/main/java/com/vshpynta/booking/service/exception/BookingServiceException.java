package com.vshpynta.booking.service.exception;

import static com.vshpynta.booking.service.rest.dto.error.PublicErrorCode.GENERAL_ERROR;

public class BookingServiceException extends RuntimeException {

    public String getErrorCode() {
        return GENERAL_ERROR.getCode();
    }

    public BookingServiceException(String message) {
        super(message);
    }

    public BookingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
