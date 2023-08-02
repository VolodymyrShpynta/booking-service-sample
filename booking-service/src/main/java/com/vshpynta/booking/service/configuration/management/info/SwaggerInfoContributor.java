package com.vshpynta.booking.service.configuration.management.info;

import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.util.Assert;

import static java.lang.String.format;

@FieldDefaults(makeFinal = true)
public class SwaggerInfoContributor implements InfoContributor {

    private String host;
    private int port;
    private String contextPath;

    public SwaggerInfoContributor(String host, int port, String contextPath) {
        Assert.notNull(host, "Host can't be null");

        this.host = host;
        this.port = port;
        this.contextPath = contextPath == null ? "" : contextPath;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("swagger", buildUrl());
    }

    private String buildUrl() {
        return format("http://%s:%s%s", host, port, contextPath + "/swagger-ui.html");
    }
}
