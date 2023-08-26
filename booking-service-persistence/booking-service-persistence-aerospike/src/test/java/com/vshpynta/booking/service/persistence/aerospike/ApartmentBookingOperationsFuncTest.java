package com.vshpynta.booking.service.persistence.aerospike;

import com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument;
import com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument.UserBooking;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static com.vshpynta.booking.service.common.utils.MappingUtils.mapItemsToList;
import static com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument.key;
import static com.vshpynta.booking.service.testing.utils.AssertUtils.assertEqualsTs;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBooking;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.FIXED_NOW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.ONE_HOUR_AFTER;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_DAYS_AFTER;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_HOURS_AFTER;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;
import static org.assertj.core.api.Assertions.assertThat;

class ApartmentBookingOperationsFuncTest extends BasePersistenceTest {

    @Test
    void shouldSuccessfullyAddBooking() {
        //given:
        var apartmentId = generatePositiveLong();

        var booking = buildApartmentBooking(apartmentId, TOMORROW, TWO_WEEKS_AFTER);

        var expectedDoc = AerospikeApartmentBookingsDocument.builder()
                .key(key(apartmentId))
                .usersBookings(Set.of(UserBooking.of(booking.getUserId(), TOMORROW, TWO_WEEKS_AFTER)))
                .documentExpirationSec(calculateTimeToFinish(TWO_WEEKS_AFTER))
                .version(1L)
                .build();

        //when:
        var resultDocument = apartmentBookingOperations.addBooking(booking);

        //then:
        assertThat(resultDocument).contains(expectedDoc);

        //and when:
        var foundDocument = apartmentBookingOperations.find(apartmentId);

        //then:
        assertThat(foundDocument).isNotEmpty();
        assertThat(foundDocument.get())
                .usingRecursiveComparison()
                .ignoringFields("usersBookings.startDate", "usersBookings.endDate")
                .isEqualTo(expectedDoc);
        assertThat(foundDocument.get().getUsersBookings()).hasSize(1);
        assertEqualsTs(mapItemsToList(foundDocument.get().getUsersBookings(), UserBooking::getStartDate).get(0),
                TOMORROW);
        assertEqualsTs(mapItemsToList(foundDocument.get().getUsersBookings(), UserBooking::getEndDate).get(0),
                TWO_WEEKS_AFTER);
    }

    @Test
    void shouldSuccessfullyAddCoupleNotOverlappingBookings() {
        //given:
        var apartmentId = generatePositiveLong();

        var bookingOne = buildApartmentBooking(apartmentId, ONE_HOUR_AFTER, TWO_HOURS_AFTER);
        var bookingTwo = buildApartmentBooking(apartmentId, TOMORROW, TWO_DAYS_AFTER);

        var expectedDoc = AerospikeApartmentBookingsDocument.builder()
                .key(key(apartmentId))
                .usersBookings(Set.of(
                        UserBooking.of(bookingOne.getUserId(), ONE_HOUR_AFTER, TWO_HOURS_AFTER),
                        UserBooking.of(bookingTwo.getUserId(), TOMORROW, TWO_DAYS_AFTER)))
                .documentExpirationSec(calculateTimeToFinish(TWO_DAYS_AFTER))
                .version(2L)
                .build();

        //when:
        var resultDocument = apartmentBookingOperations.addBooking(bookingOne);

        //then:
        assertThat(resultDocument).isNotEmpty();

        //and when:
        var secondResultDocument = apartmentBookingOperations.addBooking(bookingTwo);

        //then:
        assertThat(secondResultDocument).isNotEmpty();
        assertThat(secondResultDocument.get())
                .usingRecursiveComparison()
                .ignoringFields("usersBookings.startDate", "usersBookings.endDate")
                .ignoringCollectionOrder()
                .isEqualTo(expectedDoc);
    }

    @Test
    void shouldNotAddOverlappingBookings() {
        //given:
        var apartmentId = generatePositiveLong();

        var bookingOne = buildApartmentBooking(apartmentId, ONE_HOUR_AFTER, TOMORROW);
        var overlappingBookingTwo = buildApartmentBooking(apartmentId, TWO_HOURS_AFTER, TWO_DAYS_AFTER);

        var expectedDoc = AerospikeApartmentBookingsDocument.builder()
                .key(key(apartmentId))
                .usersBookings(Set.of(
                        UserBooking.of(bookingOne.getUserId(), ONE_HOUR_AFTER, TOMORROW)))
                .documentExpirationSec(calculateTimeToFinish(TOMORROW))
                .version(1L)
                .build();

        //when:
        var resultDocument = apartmentBookingOperations.addBooking(bookingOne);

        //then:
        assertThat(resultDocument).isNotEmpty();

        //and when:
        var secondResultDocument = apartmentBookingOperations.addBooking(overlappingBookingTwo);

        //then:
        assertThat(secondResultDocument).isEmpty();

        //and when:
        var foundDocument = apartmentBookingOperations.find(apartmentId);

        //then:
        assertThat(foundDocument).isNotEmpty();
        assertThat(foundDocument.get())
                .usingRecursiveComparison()
                .ignoringFields("usersBookings.startDate", "usersBookings.endDate")
                .isEqualTo(expectedDoc);
    }

    private Integer calculateTimeToFinish(Instant endDate) {
        return (int) endDate.minusMillis(FIXED_NOW.toEpochMilli())
                .getEpochSecond();
    }
}
