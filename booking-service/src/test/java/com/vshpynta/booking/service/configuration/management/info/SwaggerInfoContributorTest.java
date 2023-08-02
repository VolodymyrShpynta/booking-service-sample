package com.vshpynta.booking.service.configuration.management.info;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.info.Info;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SwaggerInfoContributorTest {
    private static final String HOST = "255.123.467.46";
    private static final int PORT = 8645;
    private static final String CONTEXT_PATH = "/booking-service";

    @Test
    void addsLinkToSwaggerInInfoObject() {
        var infoBuilder = new Info.Builder();

        var infoContributor = new SwaggerInfoContributor(HOST, PORT, CONTEXT_PATH);

        infoContributor.contribute(infoBuilder);

        var info = infoBuilder.build();

        assertThat(info.get("swagger"))
                .isEqualTo("http://255.123.467.46:8645/booking-service/swagger-ui.html");
    }

    @Test
    void addsLinkToSwaggerInInfoObjectWhenContextPathIsEmpty() {
        var infoBuilder = new Info.Builder();

        var infoContributor = new SwaggerInfoContributor(HOST, PORT, "");

        infoContributor.contribute(infoBuilder);

        var info = infoBuilder.build();

        assertThat(info.get("swagger"))
                .isEqualTo("http://255.123.467.46:8645/swagger-ui.html");
    }

    @Test
    void addsLinkToSwaggerInInfoObjectWhenContextPathIsNull() {
        var infoBuilder = new Info.Builder();

        var infoContributor = new SwaggerInfoContributor(HOST, PORT, null);

        infoContributor.contribute(infoBuilder);

        var info = infoBuilder.build();

        assertThat(info.get("swagger")).
                isEqualTo("http://255.123.467.46:8645/swagger-ui.html");
    }

    @Test
    void throwsIllegalArgumentExceptionWhenHostIsNull() {
        assertThatThrownBy(() -> {
            new SwaggerInfoContributor(null, PORT, CONTEXT_PATH);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}