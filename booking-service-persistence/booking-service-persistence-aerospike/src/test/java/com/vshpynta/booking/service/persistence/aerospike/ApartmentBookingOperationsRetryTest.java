package com.vshpynta.booking.service.persistence.aerospike;

import com.aerospike.client.AerospikeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;

import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBooking;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApartmentBookingOperationsRetryTest extends BasePersistenceRetryTest {

    private static final String APARTMENT_BOOKING_OPERATION_FIND = "ApartmentBookingOperations.find";

    @Value("${aerospike.operationTimeoutMs}")
    private int aerospikeOperationTimeoutMs;
    @Value("${aerospike.operations.maxAttempts}")
    private int aerospikeRetryCount;

    @Test
    void readsShouldRetry() {
        //given:
        var apartmentId = generatePositiveLong();

        var booking = buildApartmentBooking(apartmentId, TOMORROW, TWO_WEEKS_AFTER);
        var resultDocument = apartmentBookingOperations.addBooking(booking);

        //expect:
        runWithNetworkLatency(aerospikeOperationTimeoutMs,
                () -> assertThatThrownBy(() -> apartmentBookingOperations.find(apartmentId))
                        .isInstanceOf(QueryTimeoutException.class)
                        .hasCauseInstanceOf(AerospikeException.Timeout.class)
        );

        //and:
        assertThat(testRetryListener.getRetryCountFor(APARTMENT_BOOKING_OPERATION_FIND))
                .hasValue(aerospikeRetryCount);
    }
}
