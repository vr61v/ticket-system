package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.seat.SeatRequestDto;
import org.vr61v.dtos.seat.SeatResponseDto;
import org.vr61v.embedded.SeatID;
import org.vr61v.entities.Aircraft;
import org.vr61v.entities.Seat;
import org.vr61v.mappers.SeatMapper;
import org.vr61v.services.crud.AircraftCrudService;
import org.vr61v.services.crud.SeatCrudService;
import org.vr61v.services.custom.SeatCustomService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/aircrafts/{code}/seats")
public class SeatController {

    private final SeatCrudService seatCrudService;

    private final SeatCustomService seatCustomService;

    private final AircraftCrudService aircraftCrudService;

    private final SeatMapper mapper;

    public SeatController(
            SeatCrudService seatCrudService,
            SeatCustomService seatCustomService,
            AircraftCrudService aircraftCrudService,
            SeatMapper seatMapper
    ) {
        this.seatCrudService = seatCrudService;
        this.seatCustomService = seatCustomService;
        this.aircraftCrudService = aircraftCrudService;
        this.mapper = seatMapper;
    }

    private SeatID buildSeatID(String aircraftCode, String ticketNo) {
        Optional<Aircraft> aircraft = aircraftCrudService.findById(aircraftCode);
        if (aircraft.isEmpty()) {
            throw new IllegalArgumentException("aircraft not found");
        }

        return SeatID.builder()
                .aircraft(aircraft.get())
                .seatNo(ticketNo)
                .build();
    }

    private Seat buildEntity(String aircraftCode, String ticketNo, SeatRequestDto body) {
        Seat entity = mapper.toEntity(body);
        entity.setId(buildSeatID(aircraftCode, ticketNo));
        return entity;
    }

    @PostMapping("/{no}")
    public ResponseEntity<?> create(
            @PathVariable("code") String aircraftCode,
            @PathVariable("no") String ticketNo,
            @Valid @RequestBody SeatRequestDto request
    ) {
        Seat entity = buildEntity(aircraftCode, ticketNo, request);
        Seat created = seatCrudService.create(entity);
        SeatResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{no}")
    public ResponseEntity<?> update(
            @PathVariable("code") String aircraftCode,
            @PathVariable("no") String ticketNo,
            @Valid @RequestBody SeatRequestDto request
    ) {
        Seat entity = buildEntity(aircraftCode, ticketNo, request);
        Seat updated = seatCrudService.update(entity);
        SeatResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{no}")
    public ResponseEntity<?> findById(
            @PathVariable("code") String aircraftCode,
            @PathVariable("no") String ticketNo
    ) {
        SeatID entityId = buildSeatID(aircraftCode, ticketNo);
        Optional<Seat> found = seatCrudService.findById(entityId);
        if (found.isPresent()) {
            SeatResponseDto dto = mapper.toDto(found.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@PathVariable("code") String aircraftCode) {
        List<Seat> found = seatCustomService.findSeatsByAircraftCode(aircraftCode);
        List<SeatResponseDto> dtos = found.stream().map(mapper::toDto).toList();
        if (!dtos.isEmpty()) {
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<?> delete(
            @PathVariable("code") String aircraftCode,
            @PathVariable("no") String ticketNo
    ) {
        SeatID entityId = buildSeatID(aircraftCode, ticketNo);
        Optional<Seat> found = seatCrudService.findById(entityId);
        if (found.isPresent()) {
            seatCrudService.deleteById(entityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@PathVariable("code") String aircraftCode) {
        Optional<Aircraft> found = aircraftCrudService.findById(aircraftCode);
        if (found.isPresent()) {
            seatCustomService.deleteSeatsByAircraftCode(aircraftCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
