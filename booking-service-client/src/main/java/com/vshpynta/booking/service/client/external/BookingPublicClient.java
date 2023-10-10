package com.vshpynta.booking.service.client.external;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Convenient Feign client (wrapped by Hystrix) for Booking Service public clients API interface.
 */
@FeignClient(
        contextId = "booking-public-client",
        name = "${booking-service.public.client.name:booking-service}",
        path = "/booking-service/public/booking"
)
public interface BookingPublicClient {

    /**
     * Book apartment.
     *
     * @param apartmentBooking - the apartment booking details
     * @return apartment booking result {@link ApartmentBooking}.
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    ApartmentBooking bookApartment(@RequestBody ApartmentBooking apartmentBooking);

}
