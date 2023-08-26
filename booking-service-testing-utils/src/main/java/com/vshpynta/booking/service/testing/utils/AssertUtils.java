package com.vshpynta.booking.service.testing.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@UtilityClass
public class AssertUtils {

    public static void assertEqualsTs(Instant actualTs, Instant expectedTs) {
        assertEqualsTs(actualTs, expectedTs, 1);
    }

    public static void assertEqualsTs(Instant actualTs, Instant expectedTs, long offsetSec) {
        assertThat(actualTs).isNotNull()
                .isCloseTo(expectedTs, within(offsetSec, SECONDS));
    }

    public static <T> void assertEqualsOrEmpty(Collection<T> actual, Collection<T> expected) {
        Optional.ofNullable(expected)
                .filter(not(CollectionUtils::isEmpty))
                .ifPresentOrElse(
                        notEmptyExpected -> assertThat(actual).containsExactlyInAnyOrderElementsOf(notEmptyExpected),
                        () -> assertThat(actual).isNullOrEmpty());
    }
}
