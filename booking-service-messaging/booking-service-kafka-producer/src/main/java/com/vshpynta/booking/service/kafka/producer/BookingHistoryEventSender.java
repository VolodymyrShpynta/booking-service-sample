package com.vshpynta.booking.service.kafka.producer;

import com.vshpynta.booking.service.common.model.messaging.ApartmentBookingHistoryMessage;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;

@RequiredArgsConstructor
public class BookingHistoryEventSender {

    private final String endpointUri;
    private final ProducerTemplate producerTemplate;

    public void sendEvent(ApartmentBookingHistoryMessage event) {
        producerTemplate.sendBodyAndHeader(endpointUri, event, KafkaConstants.KEY,
                String.valueOf(event.getApartmentId()));
    }
}
