package com.vshpynta.booking.service.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtils {

    public static <T> Stream<T> streamOfItems(Collection<T> source) {
        return Stream.ofNullable(source)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull);
    }

    public static <T> Stream<T> streamOfItems(Collection<T> source1, Collection<T> source2) {
        return Stream.concat(
                        Stream.ofNullable(source1).flatMap(Collection::stream),
                        Stream.ofNullable(source2).flatMap(Collection::stream))
                .filter(Objects::nonNull);
    }
}
