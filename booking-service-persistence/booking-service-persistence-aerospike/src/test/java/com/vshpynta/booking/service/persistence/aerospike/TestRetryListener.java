package com.vshpynta.booking.service.persistence.aerospike;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestRetryListener implements RetryListener {
    private volatile ConcurrentMap<String, Integer> retryCounts = new ConcurrentHashMap<>();

    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback,
                                               Throwable throwable) {
        String name = (String) context.getAttribute(RetryContext.NAME);
        retryCounts.put(name, context.getRetryCount());
    }

    public Optional<Integer> getRetryCountFor(String name) {
        return retryCounts.entrySet().stream()
                .filter(e -> e.getKey().contains(name))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public void cleanupRetryCount() {
        retryCounts = new ConcurrentHashMap<>();
    }
}
