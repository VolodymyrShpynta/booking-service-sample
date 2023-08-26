package com.vshpynta.booking.service.persistence.aerospike;

import com.playtika.test.aerospike.EmbeddedAerospikeBootstrapConfiguration;
import com.playtika.test.common.spring.DockerPresenceBootstrapConfiguration;
import com.playtika.test.toxiproxy.EmbeddedToxiProxyBootstrapConfiguration;
import com.vshpynta.booking.service.persistence.aerospike.configuration.AerospikeConfiguration;
import com.vshpynta.booking.service.persistence.aerospike.configuration.AerospikeOperationsConfiguration;
import com.vshpynta.booking.service.persistence.aerospike.configuration.TimeConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.time.Clock;

import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generateRandomString;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.FIXED_CLOCK;

@SpringBootTest(
        classes = {
                DockerPresenceBootstrapConfiguration.class,
                EmbeddedToxiProxyBootstrapConfiguration.class,
                EmbeddedAerospikeBootstrapConfiguration.class,
                BasePersistenceTest.TestPersistenceConfiguration.class
        }
)
public abstract class BasePersistenceTest {

    protected String userId;

    @Autowired
    protected ApartmentBookingOperations apartmentBookingOperations;

    @BeforeEach
    void init() {
        userId = generateRandomString();
    }

    @EnableAutoConfiguration
    @Import({AerospikeConfiguration.class,
            TimeConfiguration.class,
            AerospikeOperationsConfiguration.class})
    public static class TestPersistenceConfiguration {

        @Bean
        public TestRetryListener testRetryListener() {
            return new TestRetryListener();
        }

        @Bean
        @Primary
        public Clock fixedClock() {
            return FIXED_CLOCK;
        }
    }
}
