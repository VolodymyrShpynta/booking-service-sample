package com.vshpynta.booking.service.common.model.messaging;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = PRIVATE)
public class ApartmentBookingHistoryMessage {

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
