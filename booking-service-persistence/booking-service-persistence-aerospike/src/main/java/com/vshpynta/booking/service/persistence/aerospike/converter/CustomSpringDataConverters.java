package com.vshpynta.booking.service.persistence.aerospike.converter;

import lombok.experimental.UtilityClass;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.Instant;

@UtilityClass
public class CustomSpringDataConverters {

    @WritingConverter
    public enum InstantToLongConverter implements Converter<Instant, Long> {
        INSTANCE;

        @Override
        public Long convert(Instant source) {
            return source.toEpochMilli();
        }
    }

    @ReadingConverter
    public enum LongToInstantConverter implements Converter<Long, Instant> {
        INSTANCE;

        @Override
        public Instant convert(Long source) {
            return Instant.ofEpochMilli(source);
        }
    }
}
