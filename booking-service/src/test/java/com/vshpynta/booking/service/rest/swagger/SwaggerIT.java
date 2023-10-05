package com.vshpynta.booking.service.rest.swagger;

import com.vshpynta.booking.service.BaseIT;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

class SwaggerIT extends BaseIT {

    @Test
    void shouldExposeSwaggerUi() {
        when().get(format("%s/swagger-ui.html", getServerUrl()))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void shouldExposeSwaggerApi() {
        when().get(format("%s/v3/api-docs", getServerUrl()))
                .then()
                .statusCode(SC_OK)
                .body("info.title", is("Booking Service API"))
                .body("paths", hasKey("/public/apartments"))
                .body("paths", hasKey("/public/booking"));
    }
}
