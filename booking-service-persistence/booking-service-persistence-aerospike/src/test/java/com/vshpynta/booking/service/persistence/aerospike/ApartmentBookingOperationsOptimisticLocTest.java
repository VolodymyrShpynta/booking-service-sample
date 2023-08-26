package com.vshpynta.booking.service.persistence.aerospike;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import static com.vshpynta.booking.service.common.utils.MappingUtils.mapItems;
import static com.vshpynta.booking.service.common.utils.StreamUtils.streamOfItems;
import static com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument.key;
import static com.vshpynta.booking.service.testing.utils.ConcurrentUtils.runConcurrently;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBooking;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.FIXED_NOW;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ApartmentBookingOperationsOptimisticLocTest extends BasePersistenceTest {

    @Test
    void shouldSuccessfullyAddDataConcurrently() {
        //given:
        var apartmentId = generatePositiveLong();

        var notOverlappingBookings = List.of(
                buildApartmentBooking(apartmentId, todayPlus(1), todayPlus(2)),
                buildApartmentBooking(apartmentId, todayPlus(3), todayPlus(4)),
                buildApartmentBooking(apartmentId, todayPlus(5), todayPlus(6)),
                buildApartmentBooking(apartmentId, todayPlus(7), todayPlus(8)),
                buildApartmentBooking(apartmentId, todayPlus(9), todayPlus(10)));

        var addNotOverlappingBookingsFunctions = streamOfItems(notOverlappingBookings)
                .map(this::addBookingFunction)
                .collect(toList());

        //when: concurrently add bookings
        var resultDocuments = runConcurrently(addNotOverlappingBookingsFunctions);

        //then:
        assertThat(mapItems(resultDocuments, Optional::get))
                .hasSameSizeAs(notOverlappingBookings)
                .extracting(AerospikeApartmentBookingsDocument::getKey)
                .containsOnly(key(apartmentId));

        //and when:
        var foundDocument = apartmentBookingOperations.find(apartmentId);

        //then:
        assertThat(foundDocument).isPresent();
        assertThat(foundDocument.get().getUsersBookings()).hasSameSizeAs(notOverlappingBookings);
        assertThat(foundDocument.get().getVersion()).isEqualTo(notOverlappingBookings.size());
    }

    private Callable<Optional<AerospikeApartmentBookingsDocument>> addBookingFunction(
            ApartmentBooking booking) {
        return () -> apartmentBookingOperations.addBooking(booking);
    }

    private Instant todayPlus(long amountOfDays) {
        return FIXED_NOW.plus(amountOfDays, ChronoUnit.DAYS);
    }
}
