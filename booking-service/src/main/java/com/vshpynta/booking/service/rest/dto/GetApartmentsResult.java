package com.vshpynta.booking.service.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vshpynta.booking.service.common.model.Apartment;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@AllArgsConstructor(staticName = "of")
public class GetApartmentsResult {

    @With
    @JsonProperty("apartments")
    List<Apartment> apartments;
}
