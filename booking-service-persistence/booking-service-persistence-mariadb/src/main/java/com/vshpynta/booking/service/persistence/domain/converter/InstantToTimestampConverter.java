package com.vshpynta.booking.service.persistence.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.Instant;

@Converter
public class InstantToTimestampConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant();
    }
}
