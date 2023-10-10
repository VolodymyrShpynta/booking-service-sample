package com.vshpynta.booking.service.client.external;

import com.vshpynta.booking.service.client.BaseClientIntegrationTest;
import com.vshpynta.booking.service.common.model.ApartmentBooking;
import feign.RetryableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.vshpynta.booking.service.common.utils.JsonUtils.toJson;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBooking;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("Booking public client should")
class BookingPublicClientTest extends BaseClientIntegrationTest {

    @Autowired
    private BookingPublicClient client;

    @Test
    void successfullyBookApartment() {
        //given:
        var apartmentBooking = buildApartmentBooking(generatePositiveLong(), TOMORROW, TWO_WEEKS_AFTER);

        stubBookApartmentResponse(apartmentBooking, 0);

        //when:
        var actualResult = client.bookApartment(apartmentBooking);

        //then:
        assertThat(actualResult).isEqualTo(apartmentBooking);
    }

    @Test
    void shouldFailOnBookApartment_whenRequestTimeExceedPreconfiguredTimeout() {
        //given:
        var apartmentBooking = buildApartmentBooking(generatePositiveLong(), TOMORROW, TWO_WEEKS_AFTER);

        stubBookApartmentResponse(apartmentBooking, 900); //the client specified timeout is 800ms

        //expect:
        assertThatThrownBy(() -> client.bookApartment(apartmentBooking))
                .isInstanceOf(NoFallbackAvailableException.class)
                .hasMessageContaining("No fallback available.")
                .hasCauseExactlyInstanceOf(RetryableException.class);
    }

    @Test
    void shouldFailOnBookApartment_ifServerErrorOccurred() {
        //given:
        var apartmentBooking = buildApartmentBooking(generatePositiveLong(), TOMORROW, TWO_WEEKS_AFTER);

        stubFor(post(urlEqualTo(PUBLIC_URL_PREFIX + "/booking"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJsonOf(apartmentBooking))
                .willReturn(aResponse()
                        .withStatus(INTERNAL_SERVER_ERROR.value())));

        //expect:
        assertThatExceptionOfType(NoFallbackAvailableException.class)
                .isThrownBy(() -> client.bookApartment(apartmentBooking))
                .withMessageContaining("No fallback available.");
    }

    private void stubBookApartmentResponse(ApartmentBooking apartmentBooking, int responseDelayMillis) {
        stubFor(post(urlEqualTo(PUBLIC_URL_PREFIX + "/booking"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJsonOf(apartmentBooking))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(toJson(apartmentBooking))
                        .withFixedDelay(responseDelayMillis)));
    }
}
