package com.vshpynta.booking.service.persistence.repository;

import com.vshpynta.booking.service.persistence.domain.ApartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Long>,
        JpaSpecificationExecutor<ApartmentEntity> {
}
