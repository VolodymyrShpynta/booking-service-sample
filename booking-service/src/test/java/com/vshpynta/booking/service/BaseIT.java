package com.vshpynta.booking.service;

import com.playtika.test.common.spring.DockerPresenceBootstrapConfiguration;
import com.playtika.test.mariadb.EmbeddedMariaDBBootstrapConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = {
                BookingServiceSampleApplication.class,
                DockerPresenceBootstrapConfiguration.class,
                EmbeddedMariaDBBootstrapConfiguration.class
        },
        webEnvironment = RANDOM_PORT
)
public abstract class BaseIT {

    @LocalServerPort
    protected int serverPort;

    @Value("${server.servlet.context-path}")
    protected String serverContextPath;

    protected String getServerUrl() {
        return String.format("http://localhost:%s%s", serverPort, serverContextPath);
    }
}
