package com.vshpynta.booking.service.common.model;

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
    String userId;

    @NotNull
    Instant startDate;

    @NotNull
    Instant endDate;
}
