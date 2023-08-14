package com.vshpynta.booking.service.persistence.repository;

import com.vshpynta.booking.service.persistence.domain.ApartmentBookingEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ApartmentBookingRepository extends JpaRepository<ApartmentBookingEntity, Long>,
        JpaSpecificationExecutor<ApartmentBookingEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select o from ApartmentBookingEntity o where o.apartmentId = :apartmentId " +
            "and ((o.startDate >= :startDate and o.startDate <= :endDate) " +
            "   or (o.startDate <= :startDate and o.endDate >= :startDate))")
    List<ApartmentBookingEntity> findAndLockOverlappingBookings(@Param("apartmentId") Long apartmentId,
                                                                @Param("startDate") Instant startDate,
                                                                @Param("endDate") Instant endDate);
}
