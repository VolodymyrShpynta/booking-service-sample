package com.vshpynta.booking.service.persistence.domain;

import com.vshpynta.booking.service.persistence.domain.converter.InstantToTimestampConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@Entity
@Table(name = "apartments_booking")
public class ApartmentBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", insertable = false)
    Long id;

    @Column(name = "apartment_id")
    Long apartmentId;

    @Convert(converter = InstantToTimestampConverter.class)
    @Column(name = "start_date")
    Instant startDate;

    @Convert(converter = InstantToTimestampConverter.class)
    @Column(name = "end_date")
    Instant endDate;
}
