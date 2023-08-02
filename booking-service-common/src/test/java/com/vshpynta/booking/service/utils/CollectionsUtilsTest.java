package com.vshpynta.booking.service.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CollectionsUtilsTest {

    @Test
    void successAppend() {
        assertThat(CollectionsUtils.append(List.of(1L, 2L), 3L, 5L)).isEqualTo(List.of(1L, 2L, 3L, 5L));
    }

    @Test
    void successConcat() {
        assertThat(CollectionsUtils.concat(List.of(1L, 2L), List.of(3L, 5L, 7L))).isEqualTo(List.of(1L, 2L, 3L, 5L, 7L));
    }

    private static Stream<Arguments> concatMapsSuccessfullyTestDataProvider() {
        return Stream.of(
                Arguments.of(null, null, Map.of()),
                Arguments.of(Map.of(), Map.of(), Map.of()),
                Arguments.of(Map.of("k1", "v1"), null, Map.of("k1", "v1")),
                Arguments.of(null, Map.of("k2", "v2"), Map.of("k2", "v2")),
                Arguments.of(
                        Map.of("k1", "v1"), Map.of("k2", "v2"), Map.of("k1", "v1", "k2", "v2")
                ),
                Arguments.of(
                        Map.of("k1", "v1"), Map.of(), Map.of("k1", "v1")
                ),
                Arguments.of(
                        Map.of(), Map.of("k2", "v2"), Map.of("k2", "v2")
                ),
                Arguments.of(
                        Map.of("k1", "v1"), Map.of("k1", "v2"), Map.of("k1", "v1") //skip duplicates
                ),
                Arguments.of(
                        mapOfNullable("k1", null), Map.of("k1", "v2"), Map.of("k1", "v2") //replace null values
                ),
                Arguments.of(
                        mapOfNullable(null, "v1"), Map.of("k2", "v2"), Map.of("k2", "v2")
                ),
                Arguments.of(
                        Map.of("k2", "v2"),
                        mapOfNullable("k1", null),
                        Map.of("k2", "v2")
                ),
                Arguments.of(
                        Map.of("k2", "v2"), mapOfNullable(null, null), Map.of("k2", "v2")
                ),
                Arguments.of(
                        mapOfNullable(null, null), mapOfNullable("k1", null), Map.of()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("concatMapsSuccessfullyTestDataProvider")
    <K, V> void concatMapsSuccessfully(Map<K, V> map1, Map<K, V> map2, Map<K, V> expectedMap) {
        //expect:
        assertEquals(expectedMap, CollectionsUtils.concatSkippingDuplicates(map1, map2));
    }

    @Test
    void successConvert() {
        assertThat(CollectionsUtils.convert(List.of(1L, 2L, 3L), String::valueOf)).isEqualTo(List.of("1", "2", "3"));
    }

    @Test
    void successFilterNullsInList() {
        assertThat(CollectionsUtils.filterNulls((List) null)).isEqualTo(List.of());

        var listOfNulls = asList(null, null);
        assertThat(listOfNulls).hasSize(2);
        assertThat(CollectionsUtils.filterNulls(listOfNulls)).isEqualTo(List.of());

        var listWithNulls = asList("1", "2", null, "3");
        assertThat(listWithNulls).hasSize(4);
        assertThat(CollectionsUtils.filterNulls(listWithNulls)).isEqualTo(List.of("1", "2", "3"));
    }

    @Test
    void successFilterNullsInSet() {
        assertThat(CollectionsUtils.filterNulls((Set) null)).isEqualTo(Set.of());

        var setOfNulls = new HashSet<>(asList(null, null));
        assertThat(setOfNulls).hasSize(1);
        assertThat(CollectionsUtils.filterNulls(setOfNulls)).isEqualTo(Set.of());

        var setWithNulls = new HashSet<>(asList("1", "2", null, "3"));
        assertThat(setWithNulls).hasSize(4);
        assertThat(CollectionsUtils.filterNulls(setWithNulls)).isEqualTo(Set.of("1", "2", "3"));
    }

    @Test
    void successExclude() {
        assertThat(CollectionsUtils.exclude(Set.of(1L, 2L, 4L, 7L), Set.of(2L, 4L))).isEqualTo(Set.of(1L, 7L));
    }

    private static Stream<Arguments> emptyCollectionTestDataProvider() {
        return Stream.of(
                arguments(Set.of(1L), true),
                arguments(List.of(1L, 3L), true),
                arguments(List.of(), false),
                arguments(null, false)
        );
    }

    @MethodSource("emptyCollectionTestDataProvider")
    @ParameterizedTest
    void successfullyVerifyIfIsNotEmptyCollection(Collection<?> collection, boolean expectedResult) {
        assertThat(CollectionsUtils.isNotEmpty(collection)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> collectionsSizeVerificationTestDataProvider() {
        return Stream.of(
                arguments(List.of(1L), List.of(1L), true),
                arguments(List.of(1L, 3L), List.of(5L, 17L), true),
                arguments(null, null, true),
                arguments(null, List.of(), true),
                arguments(List.of(), null, true),
                arguments(List.of(), List.of(), true),
                arguments(List.of(1), List.of(), false),
                arguments(List.of(1), null, false),
                arguments(null, List.of(4), false),
                arguments(List.of(), List.of(4), false),
                arguments(List.of(1), List.of(3, 2), false)
        );
    }

    @MethodSource("collectionsSizeVerificationTestDataProvider")
    @ParameterizedTest
    void successfullyVerifyCollectionsSize(List<?> collection1, List<?> collection2, boolean isSameSize) {
        assertThat(CollectionsUtils.hasSameSize(collection1, collection2)).isEqualTo(isSameSize);
    }

    @Test
    void successfullyConvertNullablesToList() {
        assertThat(CollectionsUtils.listOfNullables(null)).isEqualTo(List.of());
        assertThat(CollectionsUtils.listOfNullables(null, null)).isEqualTo(List.of());
        assertThat(CollectionsUtils.listOfNullables(null, 1)).isEqualTo(List.of(1));
        assertThat(CollectionsUtils.listOfNullables(2, 3)).isEqualTo(List.of(2, 3));
    }

    @Test
    void shouldSuccessfullyCreateListWithNulls() {
        //given:
        var expectedList = new ArrayList<Integer>();
        expectedList.add(null);
        expectedList.add(1);

        //when:
        var resultList = CollectionsUtils.listWithNulls(null, 1);

        //then:
        assertThat(resultList).hasSize(2)
                .isEqualTo(expectedList);
    }

    @Test
    void shouldSuccessfullyCreateListOfNull() {
        //given:
        var expectedList = new ArrayList<Integer>();
        expectedList.add(null);

        //when:
        var resultList = CollectionsUtils.listWithNulls(null);

        //then:
        assertThat(resultList).hasSize(1)
                .isEqualTo(expectedList);
    }

    @Test
    void shouldSuccessfullyCreateSetWithNulls() {
        //given:
        var expectedSet = new HashSet<Integer>();
        expectedSet.add(null);
        expectedSet.add(1);

        //when:
        var resultSet = CollectionsUtils.setWithNulls(null, 1);

        //then:
        assertThat(resultSet).hasSize(2)
                .isEqualTo(expectedSet);
    }

    @Test
    void shouldSuccessfullyCreateSetOfNull() {
        //given:
        var expectedSet = new HashSet<Integer>();
        expectedSet.add(null);

        //when:
        var resultSet = CollectionsUtils.setWithNulls(null);

        //then:
        assertThat(resultSet).hasSize(1)
                .isEqualTo(expectedSet);
    }

    private static Stream<Arguments> containsCollectionTestDataProvider() {
        return Stream.of(
                arguments(List.of(1L), List.of(1L), true),
                arguments(List.of(1L, 3L), List.of(1L, 3L, 3L), true),
                arguments(List.of(1L, 5L, 3L), List.of(1L, 3L, 3L), true),
                arguments(null, null, true),
                arguments(null, List.of(), true),
                arguments(List.of(), null, true),
                arguments(List.of(), List.of(), true),
                arguments(List.of(1), List.of(), true),
                arguments(List.of(), List.of(1L), false),
                arguments(null, List.of(4), false),
                arguments(List.of(1L), List.of(4L), false),
                arguments(List.of(1L), List.of(1L, 2L), false)
        );
    }

    @MethodSource("containsCollectionTestDataProvider")
    @ParameterizedTest
    void successfullyVerifyIfOneCollectionContainsAllElementsOfOtherCollection(List<Long> collection1,
                                                                               List<Long> collection2,
                                                                               boolean isSameSize) {
        assertThat(CollectionsUtils.containsAll(collection1, collection2)).isEqualTo(isSameSize);
    }

    private static Stream<Arguments> equalsIgnoringOrderCollectionTestDataProvider() {
        return Stream.of(
                arguments(List.of(1L), List.of(1L), true),
                arguments(List.of(1L), List.of(4L), false),
                arguments(List.of(3L, 1L, 4L), List.of(1L, 3L, 4L), true),
                arguments(List.of(1L), List.of(1L, 2L), false),
                arguments(List.of(1L, 3L), List.of(1L, 3L, 3L), false),
                arguments(List.of(1L, 5L, 3L), List.of(1L, 3L, 3L), false),
                arguments(List.of(1L, 3L, 3L), List.of(1L, 5L, 3L), false),
                arguments(null, null, true),
                arguments(null, List.of(), true),
                arguments(List.of(), null, true),
                arguments(List.of(), List.of(), true),
                arguments(List.of(1), List.of(), false),
                arguments(List.of(), List.of(1L), false),
                arguments(null, List.of(4), false)
        );
    }

    @MethodSource("equalsIgnoringOrderCollectionTestDataProvider")
    @ParameterizedTest
    void successfullyVerifyIfOneCollectionIsEqualsIgnoringOrderToOtherCollection(List<Long> collection1,
                                                                                 List<Long> collection2,
                                                                                 boolean isEquals) {
        assertThat(CollectionsUtils.isEqualsIgnoringOrder(collection1, collection2)).isEqualTo(isEquals);
    }

    private static <K, V> Map<K, V> mapOfNullable(K key, V value) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }
}