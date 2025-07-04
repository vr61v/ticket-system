package org.vr61v.controllers.v1.custom;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.TicketFlightDto;
import org.vr61v.embedded.TicketFlightID;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.entities.TicketFlight;
import org.vr61v.mappers.TicketFlightMapper;
import org.vr61v.services.impl.FlightService;
import org.vr61v.services.impl.TicketService;
import org.vr61v.services.impl.TicketFlightService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/v1/tickets/{no}/flights")
public class TicketFlightsController {

    private final TicketFlightService crudService;

    private final TicketService ticketService;

    private final FlightService flightService;

    private final TicketFlightMapper mapper;

    public TicketFlightsController(
            TicketFlightService ticketFlightService,
            TicketService ticketService,
            FlightService flightService,
            TicketFlightMapper ticketFlightMapper
    ) {
        this.crudService = ticketFlightService;
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.mapper = ticketFlightMapper;
    }

    private TicketFlightID createId(String no, Integer id) {
        Optional<Ticket> ticket = ticketService.findById(no);
        Optional<Flight> flight = flightService.findById(id);
        if (ticket.isEmpty() || flight.isEmpty()) {
            throw new IllegalArgumentException("ticket or flight not found");
        }
        return TicketFlightID.builder()
                .ticket(ticket.get())
                .flight(flight.get())
                .build();
    }

    private TicketFlight getRequestEntity(String no, Integer id, TicketFlightDto body) {
        TicketFlight entity = mapper.toEntity(body);
        entity.setId(createId(no, id));
        return entity;
    }

    private List<TicketFlight> getRequestEntityList(String no, List<TicketFlightDto> body) {
        List<TicketFlight> entities = new ArrayList<>();
        for (TicketFlightDto dto : body) {
            TicketFlight entity = mapper.toEntity(dto);
            entity.setId(createId(no, dto.getFlightId()));
            entities.add(entity);
        }

        return entities;
    }

    @PostMapping("{id}")
    public ResponseEntity<?> create(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id,
            @Valid @RequestBody TicketFlightDto body
    ) {
        log.info("Handling request to create the ticket flight entity with no:{}, id:{}", no, id);
        TicketFlight entity = getRequestEntity(no, id, body);
        log.info("Creating a new ticket flight entity with id:{}, body:{}", id, body);
        TicketFlight created = crudService.create(entity);
        TicketFlightDto dto = mapper.toDto(created);
        log.info("Success created a new ticket flight with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping()
    public ResponseEntity<?> createAll(
            @PathVariable("no") String no,
            @Valid @RequestBody List<TicketFlightDto> body
    ) {
        log.info("Handling request to create all size:{} ticket flight entities", body.size());
        List<TicketFlight> entities = getRequestEntityList(no, body);
        List<TicketFlight> created = crudService.createAll(entities);
        List<TicketFlightDto> dtos = created.stream().map(mapper::toDto).toList();
        log.info("Success created all new ticket flight entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id,
            @Valid @RequestBody TicketFlightDto body
    ) {
        log.info("Handling request to update the ticket flight entity with id:{}", id);
        TicketFlight entity = getRequestEntity(no, id, body);
        log.info("Updating the ticket flight entity with id:{}, body:{}", id, body);
        TicketFlight updated = crudService.update(entity);
        TicketFlightDto dto = mapper.toDto(updated);
        log.info("Success updated the ticket flight entity with id:{}, dto:{}", id, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateAll(
            @PathVariable("no") String no,
            @Valid @RequestBody List<TicketFlightDto> body
    ) {
        log.info("Handling request to update all size:{} ticket flight entities", body.size());
        List<TicketFlight> entities = getRequestEntityList(no, body);
        List<TicketFlight> updated = crudService.updateAll(entities);
        List<TicketFlightDto> dtos = updated.stream().map(mapper::toDto).toList();
        log.info("Success updating all ticket flight entities with dto size:{}", dtos.size());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id
    ) {
        TicketFlightID entityId = createId(no, id);
        log.info("Handling request to find the ticket flight entity with id:{}", entityId);
        Optional<TicketFlight> found = crudService.findById(entityId);
        if (found.isPresent()) {
            TicketFlightDto dto = mapper.toDto(found.get());
            log.info("Success finding the ticket flight entity with id:{}, dto:{}", entityId, dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            log.warn("No ticket flight entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No ticket flight entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll(@PathVariable("no") String ticketNo) {
        log.info("Handling request to find all ticket flight entities");
        List<TicketFlight> found = crudService.findTicketFlightsByTicketNo(ticketNo);
        List<TicketFlightDto> dtos = found.stream().map(mapper::toDto).toList();
        if (!dtos.isEmpty()) {
            log.info("Success finding all ticket flight entities with no:{} dto size:{}", ticketNo, dtos.size());
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            log.warn("No ticket flight with no:{} entities found", ticketNo);
            return new ResponseEntity<>(
                    String.format("No ticket flight with no:%s entities found", ticketNo),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(
            @PathVariable("no") String no,
            @PathVariable("id") Integer id
    ) {
        TicketFlightID entityId = createId(no, id);
        log.info("Handling request to delete the ticket flight entity with id:{}", entityId);
        Optional<TicketFlight> found = crudService.findById(entityId);
        if (found.isPresent()) {
            log.info("Deleting the ticket flight entity with id:{}", entityId);
            crudService.deleteById(entityId);
            log.info("Success deleting the ticket flight entity with id:{}", entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn("No ticket flight entity with id:{} found", entityId);
            return new ResponseEntity<>(
                    String.format("No ticket flight entity with id:%s found", entityId),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(
            @PathVariable("no") String no,
            @RequestBody List<Integer> ids
    ) {
        List<TicketFlightID> ticketFlightIds = ids
                .stream()
                .map(id -> createId(no, id))
                .collect(Collectors.toList());
        log.info("Handling request to delete all size:{} ticket flight entities", ticketFlightIds.size());
        List<TicketFlight> found = crudService.findAllById(ticketFlightIds);
        if (found.size() != ticketFlightIds.size()) {
            found.stream()
                    .map(TicketFlight::getId)
                    .forEach(ticketFlightIds::remove);
            log.warn("No ticket flight entities with ids:{} found", ticketFlightIds);
            return new ResponseEntity<>(
                    String.format("No ticket flight entities with ids:%s found", ticketFlightIds),
                    HttpStatus.NOT_FOUND
            );
        }

        log.info("Deleting all ticket flight entities with body size:{}", ticketFlightIds.size());
        crudService.deleteAll(ticketFlightIds);
        log.info("Success deleting all ticket flight entities with body size:{}", ticketFlightIds.size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
