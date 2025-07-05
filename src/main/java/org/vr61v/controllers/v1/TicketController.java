package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.vr61v.dtos.ticket.TicketRequestDto;
import org.vr61v.dtos.ticket.TicketResponseDto;
import org.vr61v.entities.Booking;
import org.vr61v.entities.Ticket;
import org.vr61v.mappers.TicketMapper;
import org.vr61v.services.impl.BookingService;
import org.vr61v.services.impl.TicketService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    private final BookingService bookingService;

    private final TicketMapper mapper;

    public TicketController(
            TicketService ticketService, BookingService bookingService,
            TicketMapper mapper
    ) {
        this.ticketService = ticketService;
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    private Ticket buildEntity(String ticketNo, TicketRequestDto request) {
        Ticket entity = mapper.toEntity(request);
        Optional<Booking> booking = bookingService.findById(request.getBookRef());
        if (booking.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        entity.setBooking(booking.get());
        entity.setTicketNo(ticketNo);
        return entity;
    }

    @PostMapping("/{no}")
    public ResponseEntity<?> create(
            @PathVariable("no") String ticketNo,
            @Valid @RequestBody TicketRequestDto request
    ) {
        Ticket entity = buildEntity(ticketNo, request);
        Ticket created = ticketService.create(entity);
        TicketResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{no}")
    public ResponseEntity<?> update(
            @PathVariable("no") String ticketNo,
            @Valid @RequestBody TicketRequestDto request
    ) {
        Ticket entity = buildEntity(ticketNo, request);
        Ticket updated = ticketService.update(entity);
        TicketResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{no}")
    public ResponseEntity<?> findById(@PathVariable("no") String ticketNo) {
        Optional<Ticket> found = ticketService.findById(ticketNo);
        if (found.isPresent()) {
            var dto = mapper.toDto(found.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<?> findAllByBookRef(@RequestParam String bookRef) {
        List<Ticket> found = ticketService.findTicketsByBookRef(bookRef);
        return new ResponseEntity<>(
                found.stream().map(mapper::toDto).toList(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<?> delete(@PathVariable("no") String ticketNo) {
        Optional<Ticket> found = ticketService.findById(ticketNo);
        if (found.isPresent()) {
            ticketService.deleteById(ticketNo);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
