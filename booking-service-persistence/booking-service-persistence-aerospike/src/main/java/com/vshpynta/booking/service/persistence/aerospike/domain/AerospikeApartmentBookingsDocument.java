package com.vshpynta.booking.service.persistence.aerospike.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;
import org.springframework.data.aerospike.annotation.Expiration;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.Instant;
import java.util.Set;

import static java.lang.String.format;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@Document(collection = "booking-service-apartment-booking-documents")
public class AerospikeApartmentBookingsDocument {

    private static final String KEY_TEMPLATE = "booking::%s";

    @Id
    @With
    String key;

    @Field
    @With
    Set<UserBooking> usersBookings;

    @Field("expirationSec")
    @Expiration
    @With
    Integer documentExpirationSec;

    @With
    @Version
    @NonFinal
    Long version;

    public static String key(long apartmentId) {
        return format(KEY_TEMPLATE, apartmentId);
    }

    @Value
    @RequiredArgsConstructor(staticName = "of")
    @Builder(toBuilder = true)
    public static class UserBooking {
        String userId;
        Instant startDate;
        Instant endDate;
    }
}
