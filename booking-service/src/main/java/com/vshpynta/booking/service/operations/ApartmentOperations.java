package com.vshpynta.booking.service.operations;

import com.vshpynta.booking.service.common.model.Apartment;

import java.util.Collection;
import java.util.List;

public interface ApartmentOperations {

    Apartment getById(long apartmentId);

    List<Apartment> findAll(Collection<Long> apartmentIds);
}
