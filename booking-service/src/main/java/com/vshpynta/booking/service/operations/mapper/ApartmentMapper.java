package com.vshpynta.booking.service.operations.mapper;

import com.vshpynta.booking.service.persistence.domain.ApartmentEntity;
import com.vshpynta.booking.service.rest.dto.Apartment;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ApartmentMapper {

    Apartment map(ApartmentEntity apartmentEntity);

    List<Apartment> map(Collection<ApartmentEntity> apartmentEntities);
}
