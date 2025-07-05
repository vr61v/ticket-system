package org.vr61v.controllers.v1;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.vr61v.dtos.ticketFlight.TicketFlightRequestDto;
import org.vr61v.dtos.ticketFlight.TicketFlightResponseDto;
import org.vr61v.entities.embedded.TicketFlightID;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.entities.TicketFlight;
import org.vr61v.mappers.TicketFlightMapper;
import org.vr61v.services.impl.FlightService;
import org.vr61v.services.impl.TicketFlightService;
import org.vr61v.services.impl.TicketService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/tickets/{no}/flights")
public class TicketFlightsController {

    private final TicketService ticketService;

    private final FlightService flightService;

    private final TicketFlightService ticketFlightService;

    private final TicketFlightMapper mapper;

    public TicketFlightsController(
            TicketService ticketService,
            FlightService flightService,
            TicketFlightService ticketFlightService,
            TicketFlightMapper ticketFlightMapper
    ) {
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.ticketFlightService = ticketFlightService;
        this.mapper = ticketFlightMapper;
    }

    private TicketFlightID buildTicketFlightID(String ticketNo, Integer flightNo) {
        Optional<Ticket> ticket = ticketService.findById(ticketNo);
        Optional<Flight> flight = flightService.findById(flightNo);
        if (ticket.isEmpty() || flight.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return TicketFlightID.builder()
                .ticket(ticket.get())
                .flight(flight.get())
                .build();
    }

    private TicketFlight buildEntity(String ticketNo, Integer flightId, TicketFlightRequestDto request) {
        TicketFlight entity = mapper.toEntity(request);
        entity.setId(buildTicketFlightID(ticketNo, flightId));
        return entity;
    }


    @PostMapping("{id}")
    public ResponseEntity<?> create(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId,
            @Valid @RequestBody TicketFlightRequestDto request
    ) {
        TicketFlight entity = buildEntity(ticketNo, flightId, request);
        TicketFlight created = ticketFlightService.create(entity);
        TicketFlightResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId,
            @Valid @RequestBody TicketFlightRequestDto request
    ) {
        TicketFlight entity = buildEntity(ticketNo, flightId, request);
        TicketFlight updated = ticketFlightService.update(entity);
        TicketFlightResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId
    ) {
        TicketFlightID entityId = buildTicketFlightID(ticketNo, flightId);
        Optional<TicketFlight> found = ticketFlightService.findById(entityId);
        if (found.isPresent()) {
            TicketFlightResponseDto dto = mapper.toDto(found.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@PathVariable("no") String ticketNo) {
        List<TicketFlight> found = ticketFlightService.findByTicketNo(ticketNo);
        return new ResponseEntity<>(
                found.stream().map(mapper::toDto).toList(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId
    ) {
        TicketFlightID entityId = buildTicketFlightID(ticketNo, flightId);
        Optional<TicketFlight> found = ticketFlightService.findById(entityId);
        if (found.isPresent()) {
            ticketFlightService.delete(entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
