package com.vshpynta.booking.service.kafka.consumer.configuration;

import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

@AllArgsConstructor
public abstract class AbstractErrorHandlingAwareRouteBuilder extends RouteBuilder {

    protected final String exceptionCounterMetric;

    @Override
    public void configure() {
        onException(Exception.class)
                .to("log:" + getClass().getCanonicalName() +
                        "?level=ERROR" +
                        "&showCaughtException=true" +
                        "&showStackTrace=true" +
                        "&showHeaders=true")
                .to("micrometer:counter:" + exceptionCounterMetric)
                .handled(true)
                .end();

        setupRoutes();
    }

    public abstract void setupRoutes();
}
