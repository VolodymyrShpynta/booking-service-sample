package com.vshpynta.booking.service.persistence.repository;

import com.vshpynta.booking.service.persistence.domain.ApartmentEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Long>,
        JpaSpecificationExecutor<ApartmentEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select o from ApartmentEntity o where o.id = :id")
    Optional<ApartmentEntity> findAndLock(@Param("id") Long id);
}
