package com.vshpynta.booking.service.persistence.aerospike.repository;

import com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument;
import org.springframework.data.repository.CrudRepository;

public interface ApartmentBookingsRepository extends CrudRepository<AerospikeApartmentBookingsDocument, String> {
}
