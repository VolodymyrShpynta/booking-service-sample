package com.vshpynta.booking.service.kafka.producer.configuration.properties;

import static java.lang.String.format;

public interface UriProvider {

    String getUri();

    static void addOptionalProperty(StringBuilder uri, String name, Object value) {
        if (value != null) {
            uri.append('&').append(name).append('=').append(value);
        }

    }

    static void addRequiredStart(StringBuilder uri, String name, String value) {
        validate(name, value);
        uri.append(value).append("?");
    }

    static void addFirstRequiredProperty(StringBuilder uri, String name, String value) {
        validate(name, value);
        uri.append(name).append('=').append(value);
    }

    static void addRequiredProperty(StringBuilder uri, String name, Object value) {
        validate(name, value);
        uri.append('&').append(name).append('=').append(value);
    }

    static void validate(String name, Object value) {
        if (value == null || "".equals(value)) {
            throw new IllegalArgumentException(format("Required parameter '%s' can not be null or empty, but was: '%s'",
                    name, value));
        }
    }
}
