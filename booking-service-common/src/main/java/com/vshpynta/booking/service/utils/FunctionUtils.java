package com.vshpynta.booking.service.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class FunctionUtils {

    public static <T> Function<T, T> peek(final Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T, R> R getOrNull(T t, Function<T, R> getter) {
        return Optional.ofNullable(t)
                .map(getter)
                .orElse(null);
    }

    public static <T, R> Function<T, R> sneakyThrows(ThrowingFunction<T, R, Exception> throwingFunction) {
        return t -> {
            try {
                return throwingFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException("Exception while applying function", e);
            }
        };
    }

    public static <T, R> Function<T, Callable<R>> toCallable(Function<T, R> function) {
        return t -> () -> function.apply(t);
    }

    @SneakyThrows
    public static <T> T getResult(Future<T> future) {
        return future.get();
    }
}
