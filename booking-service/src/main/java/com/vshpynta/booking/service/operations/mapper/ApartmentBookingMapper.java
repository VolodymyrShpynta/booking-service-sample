package com.vshpynta.booking.service.operations.mapper;

import com.vshpynta.booking.service.persistence.domain.ApartmentBookingEntity;
import com.vshpynta.booking.service.rest.dto.ApartmentBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ApartmentBookingMapper {

    @Mapping(target = "id", ignore = true)
    ApartmentBookingEntity map(ApartmentBooking source);

    ApartmentBooking map(ApartmentBookingEntity source);
}
