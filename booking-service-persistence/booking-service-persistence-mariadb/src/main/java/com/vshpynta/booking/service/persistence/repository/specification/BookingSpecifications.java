package com.vshpynta.booking.service.persistence.repository.specification;

import com.vshpynta.booking.service.persistence.domain.ApartmentBookingEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

import static org.springframework.data.jpa.domain.Specification.where;

public class BookingSpecifications {

    //TODO: cover by tests
    public static Specification<ApartmentBookingEntity> overlappingBookingSpec(ApartmentBookingEntity booking) {
        return where(equalApartmentId(booking))
                .and(datesOverlap(booking));
    }

    private static Specification<ApartmentBookingEntity> datesOverlap(ApartmentBookingEntity booking) {
        return where(
                // booking.startDate < root.startDate < booking.endDate < root.endDate
                // booking.startDate < root.startDate < root.endDate < booking.endDate
                startDateAfter(booking.getStartDate()).and(startDateBefore(booking.getEndDate())))
                // root.startDate < booking.startDate < root.endDate < booking.endDate
                // root.startDate < booking.startDate < booking.endDate < root.endDate
                .or(startDateBefore(booking.getStartDate()).and(endDateAfter(booking.getStartDate())));
    }

    private static Specification<ApartmentBookingEntity> equalApartmentId(ApartmentBookingEntity booking) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("apartmentId"), booking.getApartmentId());
    }

    private static Specification<ApartmentBookingEntity> startDateBefore(Instant date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), date);
    }

    private static Specification<ApartmentBookingEntity> startDateAfter(Instant date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("startDate"), date);
    }

    private static Specification<ApartmentBookingEntity> endDateAfter(Instant date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("endDate"), date);
    }
}
