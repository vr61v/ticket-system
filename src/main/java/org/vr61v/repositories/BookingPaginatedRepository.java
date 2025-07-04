package org.vr61v.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.vr61v.entities.Booking;

import java.time.OffsetDateTime;
import java.util.List;

public interface BookingPaginatedRepository extends PagingAndSortingRepository<Booking, String> {

    List<Booking> findBookingsByBookDateBetween(OffsetDateTime start, OffsetDateTime end);

}
