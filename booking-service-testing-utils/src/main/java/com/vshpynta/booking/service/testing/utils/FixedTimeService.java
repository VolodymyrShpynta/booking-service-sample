package com.vshpynta.booking.service.testing.utils;

import com.vshpynta.booking.service.common.service.TimeService;
import com.vshpynta.booking.service.common.service.impl.DefaultTimeService;
import lombok.NonNull;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.FIXED_CLOCK;

public class FixedTimeService implements TimeService {

    private final AtomicReference<TimeService> timeServiceRef =
            new AtomicReference<>(new DefaultTimeService(FIXED_CLOCK));

    public void moveClockForward(long delta, @NonNull TemporalUnit timeUnit) {
        Optional.of(timeServiceRef)
                .map(AtomicReference::get)
                .map(TimeService::currentTime)
                .map(currentTime -> currentTime.plus(delta, timeUnit))
                .map(newTime -> Clock.fixed(newTime, ZoneId.systemDefault()))
                .map(DefaultTimeService::new)
                .ifPresent(timeServiceRef::set);
    }

    @Override
    public Instant currentTime() {
        return timeServiceRef.get().currentTime();
    }

    @Override
    public long currentTimeMillis() {
        return timeServiceRef.get().currentTimeMillis();
    }

    @Override
    public Instant currentTime(String userId) {
        return timeServiceRef.get().currentTime(userId);
    }

    @Override
    public long currentTimeMillis(String userId) {
        return timeServiceRef.get().currentTimeMillis(userId);
    }
}
