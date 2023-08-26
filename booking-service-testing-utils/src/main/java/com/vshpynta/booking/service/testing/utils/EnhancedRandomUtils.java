package com.vshpynta.booking.service.testing.utils;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomUtils;

@UtilityClass
public class EnhancedRandomUtils {

    private static final EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .stringLengthRange(3, 15)
            .objectPoolSize(5)
            .collectionSizeRange(1, 4)
            .randomize(Integer.class, (Randomizer<Integer>) () -> RandomUtils.nextInt(1, Integer.MAX_VALUE))
            .randomize(Long.class, (Randomizer<Long>) () -> RandomUtils.nextLong(1, Long.MAX_VALUE))
            .build();

    public static EnhancedRandom enhancedRandom() {
        return enhancedRandom;
    }

    public static String generateRandomString() {
        return enhancedRandom.nextObject(String.class);
    }

    public static long generatePositiveLong() {
        return RandomUtils.nextLong(1, Long.MAX_VALUE);
    }
}
