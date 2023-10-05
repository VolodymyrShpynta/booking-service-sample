package com.vshpynta.booking.service.common.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MappingUtilsTest {

    static Stream<Arguments> mapListItemsTestData() {
        return Stream.of(
                arguments(List.of("a", "b"), List.of("A", "B")),
                arguments(List.of(), List.of()),
                arguments(null, List.of()),
                arguments(asList(null, null), List.of()),
                arguments(asList(null, "a"), List.of("A"))
        );
    }

    @ParameterizedTest
    @MethodSource("mapListItemsTestData")
    void shouldSuccessfullyMapListItems(List<String> sourceList,
                                        List<String> expectedResultList) {
        //when:
        var result = MappingUtils.mapItems(sourceList, String::toUpperCase);

        //then:
        assertThat(result).containsExactlyElementsOf(expectedResultList);
    }

    static Stream<Arguments> mapSetItemsTestData() {
        return Stream.of(
                arguments(Set.of("a", "b"), Set.of("A", "B")),
                arguments(Set.of(), Set.of()),
                arguments(null, Set.of())
        );
    }

    @ParameterizedTest
    @MethodSource("mapSetItemsTestData")
    void shouldSuccessfullyMapSetItems(Set<String> sourceList,
                                       Set<String> expectedResultList) {
        //when:
        var result = MappingUtils.mapItems(sourceList, String::toUpperCase);

        //then:
        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResultList);
    }

    static Stream<Arguments> mapItemsToSetTestData() {
        return Stream.of(
                arguments(Set.of("a", "b"), Set.of("A", "B")),
                arguments(List.of("a", "b", "B"), Set.of("A", "B")),
                arguments(null, Set.of()),
                arguments(asList(null, null), Set.of()),
                arguments(asList(null, "a"), Set.of("A"))
        );
    }

    @ParameterizedTest
    @MethodSource("mapItemsToSetTestData")
    void shouldSuccessfullyMapItemsToSet(Collection<String> source,
                                         Set<String> expectedResult) {
        //when:
        var result = MappingUtils.mapItemsToSet(source, String::toUpperCase);

        //then:
        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
    }

    static Stream<Arguments> mapItemsToListTestData() {
        return Stream.of(
                arguments(Set.of("a", "b"), List.of("A", "B")),
                arguments(List.of("a", "b", "B"), List.of("A", "B", "B")),
                arguments(null, List.of()),
                arguments(asList(null, null), List.of()),
                arguments(asList(null, "a"), List.of("A"))
        );
    }

    @ParameterizedTest
    @MethodSource("mapItemsToListTestData")
    void shouldSuccessfullyMapItemsToList(Collection<String> source,
                                          List<String> expectedResult) {
        //when:
        var result = MappingUtils.mapItemsToList(source, String::toUpperCase);

        //then:
        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
    }

    static Stream<Arguments> filterListItemsTestData() {
        return Stream.of(
                arguments(List.of("a", "b"), List.of("a")),
                arguments(Set.of("A", "b"), List.of("A")),
                arguments(List.of(), List.of()),
                arguments(null, List.of()),
                arguments(asList(null, null), List.of()),
                arguments(asList(null, "a"), List.of("a"))
        );
    }

    @ParameterizedTest
    @MethodSource("filterListItemsTestData")
    void shouldSuccessfullyFilterCollectionItems(Collection<String> sourceList,
                                                 List<String> expectedResultList) {
        //when:
        var result = MappingUtils.filterItems(sourceList, "A"::equalsIgnoreCase);

        //then:
        assertThat(result).containsExactlyElementsOf(expectedResultList);
    }

    static Stream<Arguments> mapValuesOfMapTestData() {
        return Stream.of(
                arguments(
                        Map.of("key1", "value1"),
                        Map.of("key1", "VALUE1")),
                arguments(
                        Map.of("key1", "value1", "key2", "value2"),
                        Map.of("key1", "VALUE1", "key2", "VALUE2")),
                arguments(
                        Map.of(),
                        Map.of()),
                arguments(
                        null,
                        Map.of()),
                arguments(
                        asMap(null, null, null, null),
                        Map.of()),
                arguments(
                        asMap(null, null, "key2", "value2"),
                        Map.of("key2", "VALUE2")),
                arguments(
                        asMap(null, "value1", "key2", "value2"),
                        Map.of("key2", "VALUE2")),
                arguments(
                        asMap("key1", null, "key2", "value2"),
                        Map.of("key2", "VALUE2"))
        );
    }

    @ParameterizedTest
    @MethodSource("mapValuesOfMapTestData")
    void shouldSuccessfullyMapValuesOfMap(Map<String, String> sourceMap,
                                          Map<String, String> expectedResultMap) {
        //when:
        var result = MappingUtils.mapValues(sourceMap, String::toUpperCase);

        //then:
        assertThat(result).containsExactlyInAnyOrderEntriesOf(expectedResultMap);
    }

    static Stream<Arguments> mapValuesListOfMapTestData() {
        return Stream.of(
                arguments(
                        Map.of("key1", List.of("value1")),
                        Map.of("key1", List.of("VALUE1"))),
                arguments(
                        Map.of("key1", List.of("value1"), "key2", List.of("value21", "value22")),
                        Map.of("key1", List.of("VALUE1"), "key2", List.of("VALUE21", "VALUE22"))),
                arguments(
                        Map.of(),
                        Map.of()),
                arguments(
                        null,
                        Map.of()),
                arguments(
                        asMap(null, null, null, null),
                        Map.of()),
                arguments(
                        asMap(null, null, "key2", List.of("value2")),
                        Map.of("key2", List.of("VALUE2"))),
                arguments(
                        asMap(null, List.of("value1"), "key2", List.of("value2")),
                        Map.of("key2", List.of("VALUE2"))),
                arguments(
                        asMap("key1", null, "key2", List.of("value2")),
                        Map.of("key2", List.of("VALUE2"))),
                arguments(
                        Map.of("key1", asList(null, "value1")),
                        Map.of("key1", List.of("VALUE1"))),
                arguments(
                        Map.of("key1", asList(null, null)),
                        Map.of("key1", List.of()))
        );
    }

    @ParameterizedTest
    @MethodSource("mapValuesListOfMapTestData")
    void shouldSuccessfullyMapValuesListOfMap(Map<String, List<String>> sourceMap,
                                              Map<String, List<String>> expectedResultMap) {
        //when:
        var result = MappingUtils.mapValueItems(sourceMap, String::toUpperCase);

        //then:
        assertThat(result).containsExactlyInAnyOrderEntriesOf(expectedResultMap);
    }

    @Test
    void shouldSuccessfullyMapNotNullsToList() {
        //expect:
        assertThat(MappingUtils.mapNotNullToList(null))
                .containsExactlyElementsOf(List.of());
        assertThat(MappingUtils.mapNotNullToList("a", null, "b"))
                .containsExactlyElementsOf(List.of("a", "b"));
        assertThat(MappingUtils.mapNotNullToList(null, null))
                .containsExactlyElementsOf(List.of());
    }

    private static <K, V> Map<K, V> asMap(K k1, V v1, K k2, V v2) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}
