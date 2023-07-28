package com.vshpynta.booking.service.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "of")
public class Apartment {
    long id;
    int number;
    String name;
}
