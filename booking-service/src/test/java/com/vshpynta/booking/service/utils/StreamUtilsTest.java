package com.vshpynta.booking.service.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.vshpynta.booking.service.utils.CollectionsUtils.listWithNulls;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class StreamUtilsTest {

    private static Stream<Arguments> streamOfItemsFromOneCollectionTestDataProvider() {
        return Stream.of(
                arguments(List.of(1), List.of(1)),
                arguments(List.of(11, 22), List.of(11, 22)),
                arguments(null, List.of()),
                arguments(List.of(), List.of()),
                arguments(listWithNulls(null), List.of()),
                arguments(listWithNulls(null, 4), List.of(4))
        );
    }

    @MethodSource("streamOfItemsFromOneCollectionTestDataProvider")
    @ParameterizedTest
    void streamOfItemsFromOneCollectionTest(Collection<Integer> collection, List<Integer> resultCollection) {
        assertThat(StreamUtils.streamOfItems(collection))
                .containsExactlyElementsOf(resultCollection);
    }

    private static Stream<Arguments> streamOfItemsFromTwoCollectionsTestDataProvider() {
        return Stream.of(
                arguments(List.of(1), List.of(1), List.of(1, 1)),
                arguments(List.of(11, 22), List.of(5, 7), List.of(11, 22, 5, 7)),
                arguments(null, null, List.of()),
                arguments(List.of(), null, List.of()),
                arguments(null, List.of(), List.of()),
                arguments(null, List.of(2), List.of(2)),
                arguments(List.of(2), null, List.of(2)),
                arguments(listWithNulls(null), listWithNulls(null, null), List.of()),
                arguments(listWithNulls(null), listWithNulls(null), List.of()),
                arguments(listWithNulls(null, 4), listWithNulls(null), List.of(4)),
                arguments(listWithNulls(null, null), listWithNulls(null, 4), List.of(4)),
                arguments(listWithNulls(null, 4), listWithNulls(null, 5), List.of(4, 5))
        );
    }

    @MethodSource("streamOfItemsFromTwoCollectionsTestDataProvider")
    @ParameterizedTest
    void streamOfItemsFromTwoCollectionsTest(Collection<Integer> collection1,
                                             Collection<Integer> collection2,
                                             List<Integer> resultCollection) {
        assertThat(StreamUtils.streamOfItems(collection1, collection2))
                .containsExactlyElementsOf(resultCollection);
    }
}