package com.vshpynta.booking.service.client;

import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern;
import com.vshpynta.booking.service.client.configuration.BookingPublicFeignClientAutoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Configuration;

import static com.vshpynta.booking.service.common.utils.JsonUtils.toJson;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(
        classes = {
                BaseClientIntegrationTest.TestConfiguration.class,
                BookingPublicFeignClientAutoConfiguration.class
        },
        properties = {
                "spring.main.allow-bean-definition-overriding=true"
        },
        webEnvironment = NONE
)
@AutoConfigureWireMock(port = 0)
public abstract class BaseClientIntegrationTest {

    protected static final String ADMIN_URL_PREFIX = "/booking-service/admin";
    protected static final String PUBLIC_URL_PREFIX = "/booking-service/public";


    @BeforeEach
    protected void init() {
    }

    protected EqualToJsonPattern equalToJsonOf(Object toMarshall) {
        return new EqualToJsonPattern(toJson(toMarshall), true, true);
    }

    @Configuration
    @EnableAutoConfiguration
    public static class TestConfiguration {
    }
}
