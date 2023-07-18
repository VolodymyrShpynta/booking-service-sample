package com.vshpynta.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = {
                BookingServiceSampleApplication.class
        },
        webEnvironment = RANDOM_PORT
)
class BookingServiceSampleApplicationTests {

    @Test
    void contextLoads() {
    }
}
