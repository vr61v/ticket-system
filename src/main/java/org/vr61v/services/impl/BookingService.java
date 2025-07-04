package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Booking;
import org.vr61v.repositories.BookingPaginatedRepository;
import org.vr61v.repositories.BookingRepository;
import org.vr61v.services.CrudService;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BookingService extends CrudService<Booking, String> {

    private final BookingPaginatedRepository bookingPaginatedRepository;

    public BookingService(BookingRepository repository, BookingPaginatedRepository bookingPaginatedRepository) {
        super(repository);
        this.bookingPaginatedRepository = bookingPaginatedRepository;
    }

    public List<Booking> findAllByDay(OffsetDateTime day) {
        return bookingPaginatedRepository.findBookingsByBookDateBetween(day, day.plusDays(1));
    }

}
