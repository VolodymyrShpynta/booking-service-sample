package com.vshpynta.booking.service.kafka.producer;

import org.junit.jupiter.api.Test;

import static com.vshpynta.booking.service.common.utils.JsonUtils.toJson;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBookingHistoryMessage;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;

public class BookingHistoryEventSenderFuncTest extends BaseProducerFuncTest {

    @Test
    void shouldSuccessfullySendBookingHistoryEvent() {
        //given:
        var bookingHistoryMessage = buildApartmentBookingHistoryMessage(generatePositiveLong(), TOMORROW, TWO_WEEKS_AFTER);

        //when:
        bookingHistoryEventSender.sendEvent(bookingHistoryMessage);

        //then:
        topicConsumerUtils.matchLastEvent(bookingHistoryTopic, toJson(bookingHistoryMessage));
    }
}
