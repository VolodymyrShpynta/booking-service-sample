package com.vshpynta.booking.service.persistence.aerospike;


import com.vshpynta.booking.service.common.model.ApartmentBooking;
import com.vshpynta.booking.service.common.service.TimeService;
import com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument;
import com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument.UserBooking;
import com.vshpynta.booking.service.persistence.aerospike.repository.ApartmentBookingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import static com.vshpynta.booking.service.common.utils.StreamUtils.streamOfItems;
import static com.vshpynta.booking.service.persistence.aerospike.domain.AerospikeApartmentBookingsDocument.key;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

@Retryable(
        retryFor = {
                QueryTimeoutException.class,
                TimeoutException.class,
                TransientDataAccessResourceException.class,
                OptimisticLockingFailureException.class
        },
        maxAttemptsExpression = "${aerospike.operations.maxAttempts}",
        backoff = @Backoff(
                delayExpression = "${aerospike.operations.backoff.delay}",
                multiplierExpression = "${aerospike.operations.backoff.multiplier}",
                random = true
        )
)
@RequiredArgsConstructor
@Slf4j
public class ApartmentBookingOperations {

    private static final long DEFAULT_MIN_TIME_TO_FINISH = 1L;

    private final ApartmentBookingsRepository repository;
    private final TimeService timeService;

    public Optional<AerospikeApartmentBookingsDocument> find(Long apartmentId) {
        return repository.findById(key(apartmentId))
                .map(this::filterOutExpiredBookings);
    }

    public Optional<AerospikeApartmentBookingsDocument> addBooking(ApartmentBooking booking) {
        log.debug("Start adding booking: {}", booking);
        return Optional.of(booking)
                .map(ApartmentBooking::getApartmentId)
                .map(AerospikeApartmentBookingsDocument::key)
                .map(this::findOrCreateBookingsDocument)
                .flatMap(document -> addBookingIfNoOverlaps(document, booking))
                .map(repository::save);
    }

    private AerospikeApartmentBookingsDocument findOrCreateBookingsDocument(String key) {
        return repository.findById(key)
                .orElseGet(() -> createBookingsDocument(key));
    }

    private AerospikeApartmentBookingsDocument createBookingsDocument(String key) {
        return AerospikeApartmentBookingsDocument.builder()
                .key(key)
                .usersBookings(Set.of())
                .build();
    }

    private Optional<AerospikeApartmentBookingsDocument> addBookingIfNoOverlaps(
            AerospikeApartmentBookingsDocument document,
            ApartmentBooking booking) {
        return Optional.of(document)
                .filter(doc -> hasNoBookingOverlaps(doc, booking))
                .map(doc -> addBooking(doc, booking));
    }

    private AerospikeApartmentBookingsDocument addBooking(AerospikeApartmentBookingsDocument document,
                                                          ApartmentBooking booking) {
        var resultBookings = streamOfItems(document.getUsersBookings(), List.of(toUserBooking(booking)))
                .filter(this::isNotExpired)
                .collect(toSet());
        return document.toBuilder()
                .usersBookings(resultBookings)
                .documentExpirationSec(calculateExpiration(resultBookings))
                .build();
    }

    private Integer calculateExpiration(Set<UserBooking> bookings) {
        return streamOfItems(bookings)
                .map(UserBooking::getEndDate)
                .map(this::calculateTimeToFinish)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Can't determine max expiration time for: " + bookings));
    }

    private Integer calculateTimeToFinish(Instant date) {
        long expiration = date.minusMillis(timeService.currentTimeMillis())
                .getEpochSecond();
        return (int) (expiration > 0 ? expiration : DEFAULT_MIN_TIME_TO_FINISH);
    }

    private boolean hasNoBookingOverlaps(AerospikeApartmentBookingsDocument bookingsDocument,
                                         ApartmentBooking booking) {
        return streamOfItems(bookingsDocument.getUsersBookings())
                .filter(existingBooking -> isOverlaps(existingBooking, booking))
                .findAny()
                .isEmpty();
    }

    private boolean isOverlaps(UserBooking existingBooking, ApartmentBooking newBooking) {
        return (existingBooking.getStartDate().isAfter(newBooking.getStartDate())
                && existingBooking.getStartDate().isBefore(newBooking.getEndDate()))
                ||
                (existingBooking.getStartDate().isBefore(newBooking.getStartDate())
                        && existingBooking.getEndDate().isAfter(newBooking.getStartDate()));
    }

    private AerospikeApartmentBookingsDocument filterOutExpiredBookings(AerospikeApartmentBookingsDocument document) {
        return streamOfItems(document.getUsersBookings())
                .filter(this::isNotExpired)
                .collect(collectingAndThen(toSet(), document::withUsersBookings));
    }

    private boolean isNotExpired(UserBooking booking) {
        return booking.getEndDate().isAfter(timeService.currentTime());
    }

    private UserBooking toUserBooking(ApartmentBooking booking) {
        return UserBooking.builder()
                .userId(booking.getUserId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
    }
}
