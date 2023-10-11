package com.vshpynta.booking.service.rest.client.external;

import com.vshpynta.booking.service.BaseFuncTest;
import org.junit.jupiter.api.Test;

import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBooking;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;
import static org.assertj.core.api.Assertions.assertThat;

public class BookApartmentFuncTest extends BaseFuncTest {

    @Test
    void shouldSuccessfullyBookApartment() {
        //given:
        var apartmentId = 100;
        var apartmentBooking = buildApartmentBooking(apartmentId, TOMORROW, TWO_WEEKS_AFTER);

        //when:
        var result = bookingPublicClient.bookApartment(apartmentBooking);

        //then:
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(apartmentBooking);
    }
}
