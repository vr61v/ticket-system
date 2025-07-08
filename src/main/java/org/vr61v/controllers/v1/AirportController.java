package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.airport.AirportRequestDto;
import org.vr61v.dtos.airport.AirportResponseDto;
import org.vr61v.entities.Airport;
import org.vr61v.mappers.AirportMapper;
import org.vr61v.services.impl.AirportService;
import org.vr61v.types.Locale;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {

    private final AirportService airportService;

    private final AirportMapper mapper;

    public AirportController(
            AirportService airportService,
            AirportMapper airportMapper
    ) {
        this.airportService = airportService;
        this.mapper = airportMapper;
    }

    @PostMapping("/{code}")
    public ResponseEntity<?> create(
            @PathVariable("code") String code,
            @Valid @RequestBody AirportRequestDto request
    ) {
        Airport entity = mapper.toEntity(request);
        entity.setAirportCode(code);
        Airport created = airportService.create(entity);
        AirportResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(
            @PathVariable String code,
            @Valid @RequestBody AirportRequestDto request
    ) {
        Airport entity = mapper.toEntity(request);
        entity.setAirportCode(code);
        Airport updated = airportService.update(entity);
        AirportResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findById(
            @PathVariable("code") String code,
            @RequestParam(value = "locale", required = false) Locale locale
    ) {
        Optional<Airport> found = airportService.findById(code);
        if (found.isPresent()) {
            var dto = locale == null ?
                    mapper.toDto(found.get()) :
                    mapper.toLocalizedDto(found.get(), locale);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(value = "locale", required = false) Locale locale
    ) {
        List<Airport> found = airportService.findAll();
        if (locale == null) {
            return new ResponseEntity<>(
                    found.stream().map(mapper::toDto).toList(),
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                found.stream().map(e -> mapper.toLocalizedDto(e, locale)).toList(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> delete(@PathVariable("code") String code) {
        Optional<Airport> found = airportService.findById(code);
        if (found.isPresent()) {
            airportService.delete(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
