package com.vshpynta.booking.service.operations.impl;

import com.vshpynta.booking.service.common.model.Apartment;
import com.vshpynta.booking.service.common.utils.CollectionsUtils;
import com.vshpynta.booking.service.exception.ApartmentNotFoundException;
import com.vshpynta.booking.service.operations.ApartmentOperations;
import com.vshpynta.booking.service.operations.mapper.ApartmentMapper;
import com.vshpynta.booking.service.persistence.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultApartmentOperations implements ApartmentOperations {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;

    @Override
    public Apartment getById(long apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .map(apartmentMapper::map)
                .orElseThrow(() -> new ApartmentNotFoundException(apartmentId));
    }

    @Override
    public List<Apartment> findAll(Collection<Long> apartmentIds) {
        return Optional.ofNullable(apartmentIds)
                .filter(CollectionsUtils::isNotEmpty)
                .map(this::findByIds)
                .orElseGet(this::findAll);
    }

    private List<Apartment> findByIds(Collection<Long> apartmentIds) {
        return Optional.of(apartmentIds)
                .map(apartmentRepository::findAllById)
                .map(apartmentMapper::map)
                .orElseGet(Collections::emptyList);
    }

    private List<Apartment> findAll() {
        return Optional.of(apartmentRepository.findAll())
                .map(apartmentMapper::map)
                .orElseGet(Collections::emptyList);
    }
}
