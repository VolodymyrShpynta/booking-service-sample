package com.vshpynta.booking.service.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class BookingServiceErrorResponse {

    @JsonProperty("errorCode")
    String errorCode;

    @JsonProperty("errorMessage")
    String errorMessage;
}
