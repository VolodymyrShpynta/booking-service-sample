package com.vshpynta.booking.service.common.utils;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {

    R apply(T t) throws E;
}
