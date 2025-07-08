package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.booking.BookingRequestDto;
import org.vr61v.dtos.booking.BookingResponseDto;
import org.vr61v.entities.Booking;
import org.vr61v.mappers.BookingMapper;
import org.vr61v.services.impl.BookingService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/bookings")
public class BookingController {
    
    private final BookingService bookingService;

    private final BookingMapper mapper;

    public BookingController(
            BookingService bookingService,
            BookingMapper mapper
    ) {
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @PostMapping("/{ref}")
    public ResponseEntity<?> create(
            @PathVariable("ref") String ref,
            @Valid @RequestBody BookingRequestDto request
    ) {
        Booking entity = mapper.toEntity(request);
        entity.setBookRef(ref);
        Booking created = bookingService.create(entity);
        BookingResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{ref}")
    public ResponseEntity<?> update(
            @PathVariable("ref") String ref,
            @Valid @RequestBody BookingRequestDto request
    ) {
        Booking entity = mapper.toEntity(request);
        entity.setBookRef(ref);
        Booking updated = bookingService.update(entity);
        BookingResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{ref}")
    public ResponseEntity<?> findById(@PathVariable("ref") String ref) {
        Optional<Booking> found = bookingService.findById(ref);
        if (found.isPresent()) {
            var dto = mapper.toDto(found.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<?> findAllByDay(
            @RequestParam @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate day
    ) {
        OffsetDateTime dayZoned = day.atStartOfDay().atOffset(ZoneOffset.UTC);
        List<Booking> found = bookingService.findAllByDay(dayZoned);
        return new ResponseEntity<>(
                found.stream().map(mapper::toDto).toList(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{ref}")
    public ResponseEntity<?> delete(@PathVariable("ref") String ref) {
        Optional<Booking> found = bookingService.findById(ref);
        if (found.isPresent()) {
            bookingService.delete(ref);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
