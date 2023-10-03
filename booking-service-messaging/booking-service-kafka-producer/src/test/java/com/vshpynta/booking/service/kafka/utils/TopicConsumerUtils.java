package com.vshpynta.booking.service.kafka.utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.DisposableBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONCompareMode.NON_EXTENSIBLE;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
@RequiredArgsConstructor
public class TopicConsumerUtils implements DisposableBean {

    private static final long POLL_TIMEOUT_MS = 1000;
    private static final Duration DEFAULT_TIMEOUT_DURATION = Duration.ofSeconds(5);

    private final Consumer<String, String> consumer;

    public void matchLastEvent(String topic, String expected) {
        matchLastEvent(topic, expected, DEFAULT_TIMEOUT_DURATION, true);
    }

    public void matchLastEvent(String topic, String expected, Duration timeout, boolean strictOrder) {
        matchLastEvents(topic, singletonList(expected), timeout, strictOrder);
    }

    public void matchLastEvents(String topic, List<String> expectedRecords) {
        matchLastEvents(topic, expectedRecords, DEFAULT_TIMEOUT_DURATION, true);
    }

    public void matchLastEvents(String topic, List<String> expectedRecords, Duration timeout, boolean strictOrder) {
        matchLastEvents(topic, expectedRecords, timeout, strictOrder, NON_EXTENSIBLE);
    }

    public void matchLastEvents(String topic,
                                List<String> expectedRecords,
                                Duration timeout,
                                boolean strictOrder,
                                JSONCompareMode jsonCompareMode) {
        var consumedRecords = new ArrayList<String>();
        await().timeout(timeout).untilAsserted(() -> {
            int expectedRecordsCount = Math.max(expectedRecords.size() - consumedRecords.size(), 1);
            var valueList = checkAndReturnActualMessages(topic, expectedRecordsCount, timeout).stream()
                    .map(ConsumerRecord::value)
                    .toList();
            consumedRecords.addAll(valueList);

            assertThat(consumedRecords).hasSizeGreaterThanOrEqualTo(expectedRecords.size());

            if (strictOrder) {
                for (int i = expectedRecords.size() - 1; i >= 0; i--) {
                    var actual = consumedRecords.get(consumedRecords.size() - expectedRecords.size() + i);
                    assertThat(matchJson(expectedRecords.get(i), actual, jsonCompareMode))
                            .withFailMessage("Record %s is not matched consumed record %s for index - %s",
                                    expectedRecords.get(i), actual, i)
                            .isTrue();
                }
            } else {
                expectedRecords.forEach(expectedRecord ->
                        assertThat(consumedRecords.stream()
                                .anyMatch(consumedRecord -> matchJson(expectedRecord, consumedRecord, jsonCompareMode)))
                                .withFailMessage("Record not consumed - %s from records - %s",
                                        expectedRecord, consumedRecords)
                                .isTrue());
            }
        });
    }

    private List<ConsumerRecord<String, String>> checkAndReturnActualMessages(String topic,
                                                                              int expected,
                                                                              Duration timeout) {
        var records = new ArrayList<ConsumerRecord<String, String>>();

        await().timeout(timeout).untilAsserted(() -> {
            var receivedRecords = pollForRecords(topic, Duration.ofMillis(POLL_TIMEOUT_MS));
            records.addAll(receivedRecords);
            assertThat(records.size()).isGreaterThanOrEqualTo(expected);
        });

        log.info("Received events from topic [{}] - {}", topic, records);
        return records;
    }

    private List<ConsumerRecord<String, String>> pollForRecords(String topic, Duration timeout) {
        consumer.subscribe(singleton(topic));
        var received = consumer.poll(timeout);

        if (received.isEmpty()) {
            return emptyList();
        }

        return StreamSupport.stream(received.records(topic).spliterator(), false)
                .collect(toList());
    }

    @SneakyThrows
    private static boolean matchJson(String expected, String actual, JSONCompareMode jsonCompareMode) {
        var result = JSONCompare.compareJSON(expected, actual, jsonCompareMode);
        boolean passed = result.passed();
        if (!passed) {
            log.error("Failed to assert Kafka JSON event. Message: {}", result);
        }

        return passed;
    }

    @Override
    public void destroy() {
        consumer.close();
    }
}
