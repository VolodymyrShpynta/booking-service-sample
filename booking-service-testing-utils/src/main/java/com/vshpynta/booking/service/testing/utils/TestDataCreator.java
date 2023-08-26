package com.vshpynta.booking.service.testing.utils;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
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
}
