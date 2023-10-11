package com.vshpynta.booking.service;

import com.playtika.test.aerospike.EmbeddedAerospikeBootstrapConfiguration;
import com.playtika.test.common.spring.DockerPresenceBootstrapConfiguration;
import com.playtika.test.kafka.configuration.EmbeddedKafkaBootstrapConfiguration;
import com.playtika.test.mariadb.EmbeddedMariaDBBootstrapConfiguration;
import com.vshpynta.booking.service.client.external.BookingPublicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        classes = {
                BookingServiceSampleApplication.class,
                DockerPresenceBootstrapConfiguration.class,
                EmbeddedMariaDBBootstrapConfiguration.class,
                EmbeddedAerospikeBootstrapConfiguration.class,
                EmbeddedKafkaBootstrapConfiguration.class
        },
        webEnvironment = DEFINED_PORT
)
public abstract class BaseFuncTest {

    @LocalServerPort
    protected int serverPort;

    @Value("${server.servlet.context-path}")
    protected String serverContextPath;

    @Autowired
    protected BookingPublicClient bookingPublicClient;

    protected String getServerUrl() {
        return String.format("http://localhost:%s%s", serverPort, serverContextPath);
    }
}
