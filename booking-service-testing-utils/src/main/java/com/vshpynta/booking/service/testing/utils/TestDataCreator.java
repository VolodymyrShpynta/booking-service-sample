package com.vshpynta.booking.service.testing.utils;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.common.model.messaging.ApartmentBookingHistoryMessage;
import lombok.experimental.UtilityClass;

import java.time.Instant;

import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.enhancedRandom;

@UtilityClass
public class TestDataCreator {

    public static ApartmentBooking buildApartmentBooking(long apartmentId, Instant startDate, Instant endDate) {
        return enhancedRandom().nextObject(ApartmentBooking.class).toBuilder()
                .apartmentId(apartmentId)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static ApartmentBookingHistoryMessage buildApartmentBookingHistoryMessage(long apartmentId, Instant startDate, Instant endDate) {
        return enhancedRandom().nextObject(ApartmentBookingHistoryMessage.class).toBuilder()
                .apartmentId(apartmentId)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
