package com.vshpynta.booking.service.persistence.aerospike;

import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.ToxiproxyContainer;

public abstract class BasePersistenceRetryTest extends BasePersistenceTest {

    private static final int ADDITIONAL_LATENCY_MS = 1000;

    @Autowired
    protected TestRetryListener testRetryListener;

    @Autowired
    private ToxiproxyContainer.ContainerProxy aerospikeContainerProxy;

    @AfterEach
    protected void tearDown() {
        testRetryListener.cleanupRetryCount();
    }

    @SneakyThrows
    protected void runWithNetworkLatency(long latencyMillis, Runnable runnable) {
        aerospikeContainerProxy.toxics()
                .latency("latency", ToxicDirection.DOWNSTREAM, latencyMillis + ADDITIONAL_LATENCY_MS)
                .setJitter(100);
        try {
            runnable.run();
        } finally {
            aerospikeContainerProxy.toxics().get("latency").remove();
        }
    }
}
