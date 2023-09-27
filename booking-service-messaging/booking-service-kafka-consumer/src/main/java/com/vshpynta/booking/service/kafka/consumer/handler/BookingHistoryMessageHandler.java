package com.vshpynta.booking.service.kafka.consumer.handler;

import com.vshpynta.booking.service.common.model.messaging.ApartmentBookingHistoryMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;

@Slf4j
@AllArgsConstructor
public class BookingHistoryMessageHandler {

    public void handle(@Body ApartmentBookingHistoryMessage apartmentBookingMessage) {
        log.info("Start handling apartment booking message: {}", apartmentBookingMessage);
    }
}
