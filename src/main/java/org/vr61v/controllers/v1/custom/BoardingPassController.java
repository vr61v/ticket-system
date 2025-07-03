package org.vr61v.controllers.v1.custom;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.BoardingPassDto;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.BoardingPass;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.mappers.BoardingPassMapper;
import org.vr61v.services.crud.BoardingPassCrudService;
import org.vr61v.services.crud.FlightCrudService;
import org.vr61v.services.crud.TicketCrudService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/v1/tickets/{no}/flights/{id}/boarding-pass")
public class BoardingPassController {

    private final BoardingPassCrudService boardingPassCrudService;

    private final TicketCrudService ticketCrudService;

    private final FlightCrudService flightCrudService;

    private final BoardingPassMapper mapper;

    public BoardingPassController(
            BoardingPassCrudService boardingPassCrudService,
            TicketCrudService ticketCrudService,
            FlightCrudService flightCrudService,
            BoardingPassMapper boardingPassMapper
    ) {
        this.boardingPassCrudService = boardingPassCrudService;
        this.ticketCrudService = ticketCrudService;
        this.flightCrudService = flightCrudService;
        this.mapper = boardingPassMapper;
    }

    private TicketFlightID createId(String no, Integer id) {
        Optional<Ticket> ticket = ticketCrudService.findById(no);
        Optional<Flight> flight = flightCrudService.findById(id);
        if (ticket.isEmpty() || flight.isEmpty()) {
            throw new IllegalArgumentException("ticket or flight not found");
        }
        return TicketFlightID.builder()
                .ticket(ticket.get())
                .flight(flight.get())
                .build();
    }

    private BoardingPass getRequestEntity(String no, Integer id, BoardingPassDto body) {
        BoardingPass entity = mapper.toEntity(body);
        entity.setId(createId(no, id));
        return entity;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id,
            @Valid @RequestBody BoardingPassDto body
    ) {
        log.info("Handling request to create a new boarding pass entity with no:{}, id:{}", no, id);
        BoardingPass entity = getRequestEntity(no, id, body);
        log.info("Creating a new boarding pass entity with id:{}, body:{}", id, body);
        BoardingPass created = boardingPassCrudService.create(entity);
        BoardingPassDto dto = mapper.toDto(created);
        log.info("Success creating a new boarding pass entity with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id,
            @Valid @RequestBody BoardingPassDto body
    ) {
        log.info("Handling request to update the boarding pass entity with id:{}", id);
        BoardingPass entity = getRequestEntity(no, id, body);
        log.info("Updating the boarding pass entity with id:{}, body:{}", id, body);
        BoardingPass updated = boardingPassCrudService.update(entity);
        BoardingPassDto dto = mapper.toDto(updated);
        log.info("Success updating the boarding pass entity with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findById(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id
    ) {
        log.info("Handling request to find the boarding pass entity with id:{}", id);
        TicketFlightID entityId = createId(no, id);
        Optional<BoardingPass> found = boardingPassCrudService.findById(entityId);
        if (found.isPresent()) {
            BoardingPassDto dto = mapper.toDto(found.get());
            log.info("Success finding the boarding pass entity with id:{}, dto:{}", id, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            log.warn("No boarding pass entity with no:{} id:{} found", no, id);
            return new ResponseEntity<>(
                    String.format("No boarding pass entity with no:%s id:%s found", no, id),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id
    ) {
        TicketFlightID entityId = createId(no, id);
        log.info("Handling request to delete the boarding pass entity with id:{}", entityId);
        Optional<BoardingPass> found = boardingPassCrudService.findById(entityId);
        if (found.isPresent()) {
            log.info("Deleting the boarding pass entity with id:{}", entityId);
            boardingPassCrudService.deleteById(entityId);
            log.info("Success deleting the boarding pass entity with id:{}", entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No boarding pass entity with no:{} id:{} found", no, id);
            return new ResponseEntity<>(
                    String.format("No boarding pass entity with no:%s id:%s found", no, id),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}
