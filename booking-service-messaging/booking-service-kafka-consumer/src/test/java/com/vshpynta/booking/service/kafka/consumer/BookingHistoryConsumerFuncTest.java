package com.vshpynta.booking.service.kafka.consumer;

import com.vshpynta.booking.service.kafka.BaseConsumerFuncTest;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.vshpynta.booking.service.common.utils.JsonUtils.toJson;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBookingHistoryMessage;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class BookingHistoryConsumerFuncTest extends BaseConsumerFuncTest {

    private static final Duration DEFAULT_TIMEOUT_DURATION = Duration.ofSeconds(5);

    @Test
    void shouldSuccessfullyProcessBookingHistoryEvent() {
        // given
        var apartmentId = generatePositiveLong();
        var bookingHistoryEvent = buildApartmentBookingHistoryMessage(apartmentId, TOMORROW, TWO_WEEKS_AFTER);

        // when
        topicProducerUtils.send(bookingHistoryTopic, String.valueOf(apartmentId), toJson(bookingHistoryEvent));

        // then
        await().atMost(DEFAULT_TIMEOUT_DURATION).untilAsserted(() -> {
            verify(bookingHistoryMessageHandlerSpy).handle(bookingHistoryEvent);
        });
    }
}
