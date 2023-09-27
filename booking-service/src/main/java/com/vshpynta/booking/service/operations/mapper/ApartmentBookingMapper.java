package com.vshpynta.booking.service.operations.mapper;

import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.common.model.messaging.ApartmentBookingHistoryMessage;
import com.vshpynta.booking.service.persistence.domain.ApartmentBookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ApartmentBookingMapper {

    @Mapping(target = "id", ignore = true)
    ApartmentBookingEntity map(ApartmentBooking source);

    ApartmentBooking map(ApartmentBookingEntity source);

    ApartmentBookingHistoryMessage toHistoryMessage(ApartmentBooking source);
}
