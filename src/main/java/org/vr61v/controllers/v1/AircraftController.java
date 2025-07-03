package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.aircraft.AircraftRequestDto;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.mappers.AircraftMapper;
import org.vr61v.services.crud.AircraftCrudService;
import org.vr61v.types.Locale;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/aircrafts")
public class AircraftController {

    private final AircraftCrudService aircraftCrudService;

    private final AircraftMapper mapper;

    public AircraftController(
            AircraftCrudService aircraftCrudService,
            AircraftMapper aircraftMapper
    ) {
        this.aircraftCrudService = aircraftCrudService;
        this.mapper = aircraftMapper;
    }

    @PostMapping("/{code}")
    public ResponseEntity<?> create(
            @PathVariable("code") String code,
            @Valid @RequestBody AircraftRequestDto request
    ) {
        Aircraft entity = mapper.toEntity(request);
        entity.setAircraftCode(code);
        Aircraft created = aircraftCrudService.create(entity);
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
        Aircraft updated = aircraftCrudService.update(entity);
        AircraftResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findById(
            @PathVariable("code") String code,
            @RequestParam(value = "locale", required = false) Locale locale
    ) {
        Optional<Aircraft> found = aircraftCrudService.findById(code);
        if (found.isPresent()) {
            var dto = locale == null ?
                    mapper.toDto(found.get()) :
                    mapper.toLocalizedDto(found.get(), locale);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "locale", required = false) Locale locale) {
        List<Aircraft> found = aircraftCrudService.findAll();
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
        Optional<Aircraft> found = aircraftCrudService.findById(code);
        if (found.isPresent()) {
            aircraftCrudService.deleteById(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody Set<String> ids) {
        List<Aircraft> found = aircraftCrudService.findAllById(ids);
        if (found.size() != ids.size()) {
            found.stream()
                    .map(Aircraft::getAircraftCode)
                    .forEach(ids::remove);
            return new ResponseEntity<>(
                    String.format("No aircrafts with ids:%s found", ids),
                    HttpStatus.NOT_FOUND
            );
        }

        aircraftCrudService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
