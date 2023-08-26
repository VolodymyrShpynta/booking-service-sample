package com.vshpynta.booking.service.common.service;

import java.time.Instant;


public interface TimeService {

    Instant currentTime();

    long currentTimeMillis();

    Instant currentTime(String userId);

    long currentTimeMillis(String userId);
}
