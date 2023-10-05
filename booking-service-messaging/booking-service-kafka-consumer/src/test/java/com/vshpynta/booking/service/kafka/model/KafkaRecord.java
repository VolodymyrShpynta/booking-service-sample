package com.vshpynta.booking.service.kafka.model;

import lombok.Value;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;

@Value(staticConstructor = "of")
public class KafkaRecord {

    long timestamp;
    Headers headers;
    String key;
    String value;

    public ProducerRecord<String, String> toProducerRecord(String topicName) {
        return new ProducerRecord<>(topicName, null, timestamp, key, value, headers);
    }
}
