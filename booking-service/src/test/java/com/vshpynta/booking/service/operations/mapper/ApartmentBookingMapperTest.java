package com.vshpynta.booking.service.operations.mapper;

import com.vshpynta.booking.service.persistence.domain.ApartmentBookingEntity;
import com.vshpynta.booking.service.rest.dto.ApartmentBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.enhancedRandom;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ApartmentBookingMapperTest {

    @TestConfiguration
    @ComponentScan(basePackageClasses = {ApartmentBookingMapper.class})
    public static class SpringTestConfig {
    }

    @Autowired
    private ApartmentBookingMapper mapper;

    @Test
    void shouldSuccessfullyMapFromEntity() {
        //given:
        var source = enhancedRandom().nextObject(ApartmentBookingEntity.class);

        //when:
        var result = mapper.map(source);

        //then:
        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(source);
    }

    @Test
    void shouldSuccessfullyMapToEntity() {
        //given:
        var source = enhancedRandom().nextObject(ApartmentBooking.class);

        //when:
        var result = mapper.map(source);

        //then:
        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(source);

        assertThat(result.getId()).isNull();
    }
}
