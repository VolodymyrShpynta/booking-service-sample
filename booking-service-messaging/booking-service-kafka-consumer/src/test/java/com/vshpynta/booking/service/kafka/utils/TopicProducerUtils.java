package com.vshpynta.booking.service.kafka.utils;

import com.vshpynta.booking.service.kafka.model.KafkaRecord;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;

import static java.lang.System.currentTimeMillis;

@Slf4j
@RequiredArgsConstructor
public class TopicProducerUtils {

    private final Producer<String, String> producer;

    @SneakyThrows
    public void send(String topic, KafkaRecord record) {
        producer.send(record.toProducerRecord(topic)).get();
    }

    @SneakyThrows
    public void send(String topic, String key, String value) {
        producer.send(KafkaRecord.of(currentTimeMillis(), null, key, value).toProducerRecord(topic)).get();
    }
}
