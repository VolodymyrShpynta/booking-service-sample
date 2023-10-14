package com.vshpynta.booking.service.rest.controller;

import com.vshpynta.booking.service.exception.BookingServiceException;
import org.junit.jupiter.api.Test;

import static com.vshpynta.booking.service.common.utils.JsonUtils.toJson;
import static com.vshpynta.booking.service.rest.dto.error.PublicErrorCode.GENERAL_ERROR;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestDataCreator.buildApartmentBooking;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TOMORROW;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TWO_WEEKS_AFTER;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest extends RestControllerTestBase {

    private static final String PUBLIC_BOOKING_URL = "/public/booking";

    @Test
    void shouldReturn200_whenSuccessfullyBookApartment() throws Exception {
        //given:
        var apartmentBooking = buildApartmentBooking(generatePositiveLong(), TOMORROW, TWO_WEEKS_AFTER);

        when(bookingOperations.bookApartment(apartmentBooking))
                .thenReturn(apartmentBooking);

        //expect:
        mockMvc.perform(post(PUBLIC_BOOKING_URL)
                        .contentType(APPLICATION_JSON)
                        .content(toJson(apartmentBooking)))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(apartmentBooking)))
                .andDo(document("bookingController_bookApartmentSuccessfully",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

        //and:
        verify(bookingOperations).bookApartment(apartmentBooking);
    }

    @Test
    void shouldReturn400_onBookApartment_ifValidationFailed() throws Exception {
        //given:
        var apartmentBooking = buildApartmentBooking(generatePositiveLong(), null, TWO_WEEKS_AFTER);

        //expect:
        mockMvc.perform(post(PUBLIC_BOOKING_URL)
                        .contentType(APPLICATION_JSON)
                        .content(toJson(apartmentBooking)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", equalTo(GENERAL_ERROR.getCode())))
                .andExpect(jsonPath("$.errorMessage",
                        containsString("Bad request error. Incident ID:")))
                .andDo(document("bookingController_bookApartmentValidationFailed",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));

        //and:
        verifyNoInteractions(bookingOperations);
    }

    @Test
    void shouldReturn400_onBookApartment_ifApartmentBookingExceptionOccurred() throws Exception {
        //given:
        var apartmentBooking = buildApartmentBooking(generatePositiveLong(), TOMORROW, TWO_WEEKS_AFTER);

        when(bookingOperations.bookApartment(apartmentBooking))
                .thenThrow(new BookingServiceException("Emulated error"));

        //expect:
        mockMvc.perform(post(PUBLIC_BOOKING_URL)
                        .contentType(APPLICATION_JSON)
                        .content(toJson(apartmentBooking)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode", equalTo(GENERAL_ERROR.getCode())))
                .andExpect(jsonPath("$.errorMessage",
                        containsString("Bad request error: [Emulated error]. Incident ID:")));

        //and:
        verify(bookingOperations).bookApartment(apartmentBooking);
    }
}
