package com.vshpynta.booking.service.persistence.aerospike.repository;

import com.vshpynta.booking.service.persistence.aerospike.BasePersistenceTest;
import com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument.key;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.enhancedRandom;
import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.generatePositiveLong;
import static com.vshpynta.booking.service.testing.utils.TestTimeUtils.TEN_MINUTES_IN_SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class ApartmentBookingsRepositoryTest extends BasePersistenceTest {

    @Autowired
    private ApartmentBookingsRepository apartmentBookingsRepository;

    @Test
    void shouldSuccessfullySaveAndRead() {
        //given:
        var bookingsDocument = enhancedRandom().nextObject(AerospikeApartmentBookingsDocument.class)
                .withKey(key(generatePositiveLong()))
                .withDocumentExpirationSec(TEN_MINUTES_IN_SECONDS)
                .withVersion(null);

        //when:
        apartmentBookingsRepository.save(bookingsDocument);

        //and:
        var foundDocument = apartmentBookingsRepository.findById(bookingsDocument.getKey());

        //then:
        assertThat(foundDocument).hasValue(bookingsDocument);
    }
}
