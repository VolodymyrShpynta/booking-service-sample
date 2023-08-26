package com.vshpynta.booking.service.testing.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ConcurrentUtils {

    @SafeVarargs
    @SneakyThrows
    public static <T> List<T> runConcurrently(Callable<T>... callables) {
        return runConcurrently(List.of(callables));
    }

    @SneakyThrows
    public static <T> List<T> runConcurrently(Collection<Callable<T>> callables) {
        return runConcurrently(callables.size(), callables.stream());
    }

    @SneakyThrows
    public static <T> List<T> runConcurrently(int streamSize, Stream<Callable<T>> callablesStream) {
        var executor = Executors.newFixedThreadPool(streamSize);
        var countDownLatch = new CountDownLatch(streamSize);

        var waitingCallables = callablesStream
                .map(callable -> waitAndProcessCallable(countDownLatch, callable))
                .collect(toList());

        return executor.invokeAll(waitingCallables).stream()
                .map(ConcurrentUtils::getResult)
                .collect(toList());
    }

    @SneakyThrows
    private static <T> T getResult(Future<T> future) {
        return future.get();
    }

    @SneakyThrows
    private static <T> Callable<T> waitAndProcessCallable(CountDownLatch countDownLatch, Callable<T> callable) {
        return () -> {
            countDownLatch.countDown();
            countDownLatch.await();
            return callable.call();
        };
    }
}
