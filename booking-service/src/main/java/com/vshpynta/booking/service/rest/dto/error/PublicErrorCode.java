package com.vshpynta.booking.service.rest.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublicErrorCode {
    GENERAL_ERROR("GENERAL_ERROR"),
    APARTMENT_NOT_FOUND("APARTMENT_NOT_FOUND");

    private final String code;
}
