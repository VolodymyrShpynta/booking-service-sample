package com.vshpynta.booking.service.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "of")
public class ApartmentBooking {

    Long id;

    @NotNull
    Long apartmentId;

    @NotNull
    Instant startDate;

    @NotNull
    Instant endDate;
}