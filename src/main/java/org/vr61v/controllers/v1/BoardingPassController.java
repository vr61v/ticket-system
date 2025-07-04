package org.vr61v.controllers.v1;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.boardingPass.BoardingPassRequestDto;
import org.vr61v.dtos.boardingPass.BoardingPassResponseDto;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.BoardingPass;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.mappers.BoardingPassMapper;
import org.vr61v.services.impl.BoardingPassService;
import org.vr61v.services.impl.FlightService;
import org.vr61v.services.impl.TicketService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/tickets/{no}/flights/{id}/boarding-pass")
public class BoardingPassController {

    private final BoardingPassService boardingPassService;

    private final TicketService ticketService;

    private final FlightService flightService;

    private final BoardingPassMapper mapper;

    public BoardingPassController(
            BoardingPassService boardingPassService,
            TicketService ticketService,
            FlightService flightService,
            BoardingPassMapper boardingPassMapper
    ) {
        this.boardingPassService = boardingPassService;
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.mapper = boardingPassMapper;
    }

    private TicketFlightID buildTicketFlightID(String ticketNo, Integer flightNo) {
        Optional<Ticket> ticket = ticketService.findById(ticketNo);
        Optional<Flight> flight = flightService.findById(flightNo);
        if (ticket.isEmpty() || flight.isEmpty()) {
            throw new IllegalArgumentException("ticket or flight not found");
        }
        return TicketFlightID.builder()
                .ticket(ticket.get())
                .flight(flight.get())
                .build();
    }

    private BoardingPass buildEntity(String ticketNo, Integer flightId, BoardingPassRequestDto body) {
        BoardingPass entity = mapper.toEntity(body);
        entity.setId(buildTicketFlightID(ticketNo, flightId));
        return entity;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId,
            @Valid @RequestBody BoardingPassRequestDto request
    ) {
        BoardingPass entity = buildEntity(ticketNo, flightId, request);
        BoardingPass created = boardingPassService.create(entity);
        BoardingPassResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId,
            @Valid @RequestBody BoardingPassRequestDto request
    ) {
        BoardingPass entity = buildEntity(ticketNo, flightId, request);
        BoardingPass updated = boardingPassService.update(entity);
        BoardingPassResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findById(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId
    ) {
        TicketFlightID entityId = buildTicketFlightID(ticketNo, flightId);
        Optional<BoardingPass> found = boardingPassService.findById(entityId);
        if (found.isPresent()) {
            BoardingPassResponseDto dto = mapper.toDto(found.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(
            @PathVariable("no") String ticketNo,
            @PathVariable("id") Integer flightId
    ) {
        TicketFlightID entityId = buildTicketFlightID(ticketNo, flightId);
        Optional<BoardingPass> found = boardingPassService.findById(entityId);
        if (found.isPresent()) {
            boardingPassService.deleteById(entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
