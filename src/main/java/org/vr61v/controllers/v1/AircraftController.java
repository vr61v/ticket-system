package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.aircraft.AircraftRequestDto;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.mappers.AircraftMapper;
import org.vr61v.services.impl.AircraftService;
import org.vr61v.types.Locale;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/aircrafts")
public class AircraftController {

    private final AircraftService aircraftService;

    private final AircraftMapper mapper;

    public AircraftController(
            AircraftService aircraftService,
            AircraftMapper aircraftMapper
    ) {
        this.aircraftService = aircraftService;
        this.mapper = aircraftMapper;
    }

    @PostMapping("/{code}")
    public ResponseEntity<?> create(
            @PathVariable("code") String code,
            @Valid @RequestBody AircraftRequestDto request
    ) {
        Aircraft entity = mapper.toEntity(request);
        entity.setAircraftCode(code);
        Aircraft created = aircraftService.create(entity);
        AircraftResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(
            @PathVariable String code,
            @Valid @RequestBody AircraftRequestDto request
    ) {
        Aircraft entity = mapper.toEntity(request);
        entity.setAircraftCode(code);
        Aircraft updated = aircraftService.update(entity);
        AircraftResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findById(
            @PathVariable("code") String code,
            @RequestParam(value = "locale", required = false) Locale locale
    ) {
        Optional<Aircraft> found = aircraftService.findById(code);
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
        List<Aircraft> found = aircraftService.findAll();
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
        Optional<Aircraft> found = aircraftService.findById(code);
        if (found.isPresent()) {
            aircraftService.deleteById(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
