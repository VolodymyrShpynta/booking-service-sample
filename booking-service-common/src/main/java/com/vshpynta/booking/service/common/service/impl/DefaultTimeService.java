package com.vshpynta.booking.service.common.service.impl;

import com.vshpynta.booking.service.common.service.TimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
public class DefaultTimeService implements TimeService {

    private final Clock clock;

    @Override
    public Instant currentTime() {
        return clock.instant();
    }

    @Override
    public long currentTimeMillis() {
        return currentTime().toEpochMilli();
    }

    @Override
    public Instant currentTime(String userId) {
        return Instant.ofEpochMilli(currentTimeMillis(userId));
    }

    @Override
    public long currentTimeMillis(String userId) {
        return clock.millis();
    }
}
