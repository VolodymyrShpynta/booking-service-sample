package com.vshpynta.booking.service.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UuidGenerator {

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
