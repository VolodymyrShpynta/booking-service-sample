package com.vshpynta.booking.service.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.vshpynta.booking.service.common.utils.StreamUtils.streamOfItems;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableList;

@UtilityClass
public class MappingUtils {

    public static <T, R> List<R> mapItems(Collection<T> source,
                                          Function<? super T, ? extends R> itemMapper) {
        return streamOfItems(source)
                .map(itemMapper)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public static <T, R> Set<R> mapItems(Set<T> source,
                                         Function<? super T, ? extends R> itemMapper) {
        return streamOfItems(source)
                .map(itemMapper)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    public static <T, R> Set<R> mapItemsToSet(Collection<T> source,
                                              Function<? super T, ? extends R> itemMapper) {
        return streamOfItems(source)
                .map(itemMapper)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    public static <T, R> List<R> mapItemsToList(Collection<T> source,
                                                Function<? super T, ? extends R> itemMapper) {
        return streamOfItems(source)
                .map(itemMapper)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public static <T> List<T> filterItems(Collection<T> source,
                                          Predicate<? super T> predicate) {
        return streamOfItems(source)
                .filter(predicate)
                .collect(toList());
    }

    public static <K, V, R> Map<K, R> mapValues(Map<K, V> sourceMap,
                                                Function<V, R> valueMapper) {
        return Optional.ofNullable(sourceMap)
                .map(Map::entrySet)
                .map(entries -> streamOfItems(entries)
                        .filter(entry -> nonNull(entry.getKey()))
                        .filter(entry -> nonNull(entry.getValue()))
                        .collect(toMap(Map.Entry::getKey, entry -> valueMapper.apply(entry.getValue()))))
                .orElseGet(Collections::emptyMap);
    }

    public static <K, V, R> Map<K, List<R>> mapValueItems(Map<K, List<V>> sourceMap,
                                                          Function<V, R> valueItemMapper) {

        return Optional.ofNullable(sourceMap)
                .map(Map::entrySet)
                .map(entries -> streamOfItems(entries)
                        .filter(entry -> nonNull(entry.getKey()))
                        .filter(entry -> nonNull(entry.getValue()))
                        .collect(toMap(Map.Entry::getKey, entry -> mapItems(entry.getValue(), valueItemMapper))))
                .orElseGet(Collections::emptyMap);
    }

    @SafeVarargs
    public static <V> List<V> mapNotNullToList(final V... objects) {
        return Optional.ofNullable(objects)
                .stream()
                .flatMap(Stream::of)
                .filter(Objects::nonNull)
                .collect(toUnmodifiableList());
    }
}
