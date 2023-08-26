package com.vshpynta.booking.service.rest.controller;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.operations.BookingOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Apartments booking clients API")
@RestController
@RequestMapping(value = "/public/booking", produces = APPLICATION_JSON_VALUE)
public class BookingController {

    private final BookingOperations bookingOperations;

    /**
     * Book apartment.
     *
     * @param apartmentBooking - the apartment booking details
     * @return apartment booking result {@link ApartmentBooking}.
     */
    @Operation(summary = "Book an apartment", responses = {
            @ApiResponse(responseCode = "200", description = "Apartment was successfully booked")})
    @PostMapping
    public ApartmentBooking bookApartment(
            @Valid @RequestBody ApartmentBooking apartmentBooking) {
        log.info("Start booking apartment: {}", apartmentBooking);
        var result = bookingOperations.bookApartment(apartmentBooking);
        log.info("Finished booking apartment: {}. Result: {}", apartmentBooking, result);
        return result;
    }

    /**
     * Get apartments bookings.
     *
     * @return apartments bookings collection {@link ApartmentBooking}.
     */
    @Operation(summary = "Get apartments booking", responses = {
            @ApiResponse(responseCode = "200", description = "Apartments bookings successfully retrieved")})
    @GetMapping
    public List<ApartmentBooking> getApartmentsBookings() {
        log.info("Start getting apartments bookings");
        var result = bookingOperations.getApartmentsBookings();
        log.info("Finished getting apartments bookings. Result: {}", result);
        return result;
    }
}
