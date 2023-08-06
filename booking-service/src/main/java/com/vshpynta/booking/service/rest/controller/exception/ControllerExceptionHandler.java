package com.vshpynta.booking.service.rest.controller.exception;

import com.vshpynta.booking.service.exception.BookingServiceException;
import com.vshpynta.booking.service.rest.controller.ApartmentsController;
import com.vshpynta.booking.service.rest.controller.BookingController;
import com.vshpynta.booking.service.rest.dto.BookingServiceErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.vshpynta.booking.service.rest.dto.error.PublicErrorCode.GENERAL_ERROR;
import static com.vshpynta.booking.service.utils.UuidGenerator.generateId;
import static java.lang.String.format;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice(assignableTypes = {
        ApartmentsController.class,
        BookingController.class
})
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            DataIntegrityViolationException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
    })
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public BookingServiceErrorResponse onClientFaultException(HttpServletRequest httpRequest,
                                                              Exception exception) {
        var incidentId = generateId();
        log.warn("Bad request error occurred. Incident ID: {}. Cause: {}",
                incidentId, getRootCauseMessage(exception), exception);
        return BookingServiceErrorResponse.builder()
                .errorCode(GENERAL_ERROR.getCode())
                .errorMessage(format("Bad request error. Incident ID: [%s]", incidentId))
                .build();
    }

    @ExceptionHandler({BookingServiceException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public BookingServiceErrorResponse onBookingServiceException(HttpServletRequest httpRequest,
                                                                 BookingServiceException exception) {
        var incidentId = generateId();
        log.warn("Service error occurred. Incident ID: {}. Cause: {}",
                incidentId, getRootCauseMessage(exception), exception);
        return BookingServiceErrorResponse.builder()
                .errorCode(exception.getErrorCode())
                .errorMessage(format("Bad request error: [%s]. Incident ID: [%s]", exception.getMessage(), incidentId))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BookingServiceErrorResponse onUnexpectedException(HttpServletRequest httpRequest,
                                                             Exception exception) {
        var incidentId = generateId();
        log.error("Unexpected exception occurred. Incident ID: {}. Cause: {}",
                incidentId, getRootCauseMessage(exception), exception);
        return BookingServiceErrorResponse.builder()
                .errorCode(GENERAL_ERROR.getCode())
                .errorMessage(format("Unexpected error. Incident ID: [%s]", incidentId))
                .build();
    }
}
