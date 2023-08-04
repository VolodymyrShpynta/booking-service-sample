package com.vshpynta.booking.service.operations.mapper;

import com.vshpynta.booking.service.persistence.domain.ApartmentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.vshpynta.booking.service.testing.utils.EnhancedRandomUtils.enhancedRandom;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ApartmentMapperTest {

    @TestConfiguration
    @ComponentScan(basePackageClasses = {ApartmentMapper.class})
    public static class SpringTestConfig {
    }

    @Autowired
    private ApartmentMapper mapper;

    @Test
    void shouldSuccessfullyMapFromEntity() {
        //given:
        var source = enhancedRandom().nextObject(ApartmentEntity.class);

        //when:
        var result = mapper.map(source);

        //then:
        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(source);
    }

    @Test
    void shouldSuccessfullyMapFromEntitiesList() {
        //given:
        var source = enhancedRandom().objects(ApartmentEntity.class, 3).collect(toList());

        //when:
        var result = mapper.map(source);

        //then:
        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(source);
    }
}
