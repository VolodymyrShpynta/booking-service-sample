package com.vshpynta.booking.service.kafka.producer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.apache.camel.component.jackson.JacksonDataFormat;

@UtilityClass
public class JacksonDataFormatFactory {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .registerModule(new JavaTimeModule());

    public static JacksonDataFormat createJacksonDataFormat(Class<?> unmarshalType) {
        var jacksonDataFormat = new JacksonDataFormat(unmarshalType);
        jacksonDataFormat.setObjectMapper(objectMapper);
        return jacksonDataFormat;
    }
}
