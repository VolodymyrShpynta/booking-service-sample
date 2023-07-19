package com.vshpynta.booking.service;

import com.playtika.test.common.spring.DockerPresenceBootstrapConfiguration;
import com.playtika.test.mariadb.EmbeddedMariaDBBootstrapConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = {
                BookingServiceSampleApplication.class,
                DockerPresenceBootstrapConfiguration.class,
                EmbeddedMariaDBBootstrapConfiguration.class
        },
        properties = {
                "embedded.mariadb.database=booking_db"
        },
        webEnvironment = RANDOM_PORT
)
class BookingServiceSampleApplicationTests {

    @Test
    void contextLoads() {
    }
}
