package com.vshpynta.booking.service.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.vshpynta.booking.service.utils.StreamUtils.streamOfItems;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

@UtilityClass
public class CollectionsUtils {

    public static <T> List<T> append(List<T> list, T... args) {
        return Stream.concat(list.stream(), Stream.of(args))
                .collect(toList());
    }

    public static <T> List<T> concat(List<T> list1, List<T> list2) {
        return streamOfItems(list1, list2)
                .collect(toList());
    }

    public static <K, V> Map<K, V> concatSkippingDuplicates(Map<K, V> source1, Map<K, V> source2) {
        return Stream.concat(
                        Stream.ofNullable(source1).map(Map::entrySet).flatMap(Collection::stream),
                        Stream.ofNullable(source2).map(Map::entrySet).flatMap(Collection::stream))
                .filter(Objects::nonNull)
                .filter(kvEntry -> nonNull(kvEntry.getKey()))
                .filter(kvEntry -> nonNull(kvEntry.getValue()))
                .collect(HashMap::new,
                        (kvMap, kvEntry) -> kvMap.putIfAbsent(kvEntry.getKey(), kvEntry.getValue()), //skip duplicates
                        HashMap::putAll);
    }

    public static <T, R> List<R> convert(List<T> list, Function<T, R> itemConverter) {
        return list.stream()
                .map(itemConverter)
                .collect(toList());
    }

    public static <T> List<T> filterNulls(List<T> list) {
        if (isNull(list)) {
            return List.of();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public static <T> Set<T> filterNulls(Set<T> list) {
        if (isNull(list)) {
            return Set.of();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    public static <T> List<T> listOfNullables(T... items) {
        if (isNull(items)) {
            return List.of();
        }
        return Arrays.stream(items)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public static <T> Set<T> exclude(Set<T> source, Set<T> targetToExclude) {
        return source.stream()
                .filter(not(targetToExclude::contains))
                .collect(toSet());
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean hasSameSize(Collection<?> collection1, Collection<?> collection2) {
        int collectionOneSize = Optional.ofNullable(collection1)
                .map(Collection::size)
                .orElse(0);
        int collectionTwoSize = Optional.ofNullable(collection2)
                .map(Collection::size)
                .orElse(0);
        return collectionOneSize == collectionTwoSize;
    }

    public static <T> boolean containsAll(Collection<T> source, Collection<T> candidates) {
        if (isEmpty(candidates)) {
            return true;
        }

        if (isEmpty(source)) {
            return false;
        }

        return Set.copyOf(source)
                .containsAll(candidates);
    }

    public static <T> boolean isEqualsIgnoringOrder(List<T> actual, List<T> expected) {
        var actualSet = streamOfItems(actual).collect(toSet());
        var expectedSet = streamOfItems(expected).collect(toSet());
        return hasSameSize(actual, expected)
                && actualSet.containsAll(expectedSet)
                && expectedSet.containsAll(actualSet);
    }

    public static <T> List<T> listWithNulls(T... items) {
        if (isNull(items)) {
            var result = new ArrayList<T>();
            result.add(null);
            return result;
        }
        return Arrays.stream(items)
                .collect(toList());
    }

    public static <T> Set<T> setWithNulls(T... items) {
        if (isNull(items)) {
            var result = new HashSet<T>();
            result.add(null);
            return result;
        }
        return Arrays.stream(items)
                .collect(toSet());
    }
}
