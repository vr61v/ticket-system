package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vr61v.dtos.airport.AirportRequestDto;
import org.vr61v.dtos.airport.AirportResponseDto;
import org.vr61v.entities.Airport;
import org.vr61v.mappers.AirportMapper;
import org.vr61v.services.crud.AirportCrudService;
import org.vr61v.types.Locale;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {

    private final AirportCrudService airportCrudService;

    private final AirportMapper mapper;

    public AirportController(
            AirportCrudService airportCrudService,
            AirportMapper airportMapper
    ) {
        this.airportCrudService = airportCrudService;
        this.mapper = airportMapper;
    }

    @PostMapping("/{code}")
    public ResponseEntity<?> create(
            @PathVariable("code") String code,
            @Valid @RequestBody AirportRequestDto request
    ) {
        Airport entity = mapper.toEntity(request);
        entity.setAirportCode(code);
        Airport created = airportCrudService.create(entity);
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
        Airport updated = airportCrudService.update(entity);
        AirportResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findById(
            @PathVariable("code") String code,
            @RequestParam(value = "locale", required = false) Locale locale
    ) {
        Optional<Airport> found = airportCrudService.findById(code);
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
        List<Airport> found = airportCrudService.findAll();
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
        Optional<Airport> found = airportCrudService.findById(code);
        if (found.isPresent()) {
            airportCrudService.deleteById(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestBody Set<String> ids) {
        List<Airport> found = airportCrudService.findAllById(ids);
        if (found.size() != ids.size()) {
            found.stream()
                    .map(Airport::getAirportCode)
                    .forEach(ids::remove);
            return new ResponseEntity<>(
                    String.format("No airports with ids:%s found", ids),
                    HttpStatus.NOT_FOUND
            );
        }

        airportCrudService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
