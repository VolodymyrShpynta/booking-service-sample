package com.vshpynta.booking.service.testing.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;

@UtilityClass
public class TestTimeUtils {

    public static final Clock SYSTEM_CLOCK = Clock.systemUTC();
    public static final Clock FIXED_CLOCK = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    public static final Instant FIXED_NOW = FIXED_CLOCK.instant();

    public static final Instant FOUR_DAYS_BEFORE = FIXED_NOW.minus(4, ChronoUnit.DAYS);
    public static final Instant TWO_DAYS_BEFORE = FIXED_NOW.minus(2, ChronoUnit.DAYS);
    public static final Instant YESTERDAY = FIXED_NOW.minus(1, ChronoUnit.DAYS);
    public static final Instant ONE_HOUR_BEFORE = FIXED_NOW.minus(1, ChronoUnit.HOURS);
    public static final Instant ONE_MINUTE_BEFORE = FIXED_NOW.minus(1, ChronoUnit.MINUTES);

    public static final Instant ONE_HOUR_AFTER = FIXED_NOW.plus(1, ChronoUnit.HOURS);
    public static final Instant TWO_HOURS_AFTER = FIXED_NOW.plus(2, ChronoUnit.HOURS);
    public static final Instant TOMORROW = FIXED_NOW.plus(1, ChronoUnit.DAYS);
    public static final Instant TWO_DAYS_AFTER = FIXED_NOW.plus(2, ChronoUnit.DAYS);
    public static final Instant TWO_WEEKS_AFTER = FIXED_NOW.plus(14, ChronoUnit.DAYS);

    public static final int THREE_MINUTES_IN_SECONDS = (int) MINUTES.toSeconds(3);
    public static final int TEN_MINUTES_IN_SECONDS = (int) MINUTES.toSeconds(10);
    public static final int FIFTEEN_MINUTES_IN_SECONDS = (int) MINUTES.toSeconds(15);
    public static final int ONE_HOUR_IN_SECONDS = (int) HOURS.toSeconds(1);
    public static final int TWO_HOUR_IN_SECONDS = (int) HOURS.toSeconds(2);

    public static final long TEN_MINUTES_IN_MILLIS = TEN_MINUTES_IN_SECONDS * 1000L;

    @SneakyThrows
    public static void await(long timeout, TimeUnit unit) {
        new CountDownLatch(1).await(timeout, unit);
    }
}
