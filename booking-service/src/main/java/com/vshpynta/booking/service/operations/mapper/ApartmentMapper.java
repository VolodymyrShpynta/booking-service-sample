package com.vshpynta.booking.service.operations.mapper;

import com.vshpynta.booking.service.persistence.domain.ApartmentEntity;
import com.vshpynta.booking.service.rest.dto.Apartment;
import com.vshpynta.booking.service.utils.StreamUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
//TODO: use mapstruct
public class ApartmentMapper {

    public Apartment map(ApartmentEntity apartmentEntity) {
        return Optional.ofNullable(apartmentEntity)
                .map(entity -> Apartment.builder()
                        .id(entity.getId())
                        .number(entity.getNumber())
                        .name(entity.getName())
                        .build())
                .orElse(null);
    }

    public List<Apartment> map(Collection<ApartmentEntity> apartmentEntities) {
        return StreamUtils.streamOfItems(apartmentEntities)
                .map(this::map)
                .collect(toList());
    }
}
