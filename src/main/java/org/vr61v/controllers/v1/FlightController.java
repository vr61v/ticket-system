package org.vr61v.controllers.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.vr61v.dtos.flight.FlightRequestDto;
import org.vr61v.dtos.flight.FlightResponseDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.entities.Airport;
import org.vr61v.entities.Flight;
import org.vr61v.mappers.FlightMapper;
import org.vr61v.services.impl.AircraftService;
import org.vr61v.services.impl.AirportService;
import org.vr61v.services.impl.FlightService;
import org.vr61v.types.Locale;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/flights")
public class FlightController {

    private final AircraftService aircraftService;

    private final AirportService airportService;

    private final FlightService flightService;

    private final FlightMapper mapper;

    public FlightController(
            AircraftService aircraftService, AirportService airportService, FlightService flightService,
            FlightMapper flightMapper
    ) {
        this.aircraftService = aircraftService;
        this.airportService = airportService;
        this.flightService = flightService;
        this.mapper = flightMapper;
    }

    private Flight buildEntity(FlightRequestDto request) {
        Flight flight = mapper.toEntity(request);

        Optional<Aircraft> aircraft = aircraftService.findById(request.getAircraftCode());
        Optional<Airport> arrivalAirport = airportService.findById(request.getArrivalAirportCode());
        Optional<Airport> departureAirport = airportService.findById(request.getDepartureAirportCode());
        if (aircraft.isEmpty() || arrivalAirport.isEmpty() || departureAirport.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        flight.setAircraft(aircraft.get());
        flight.setArrivalAirport(arrivalAirport.get());
        flight.setDepartureAirport(departureAirport.get());
        return flight;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody FlightRequestDto request) {
        Flight entity = buildEntity(request);
        Flight created = flightService.create(entity);
        FlightResponseDto dto = mapper.toDto(created);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Integer flightId,
            @Valid @RequestBody FlightRequestDto request
    ) {
        Flight entity = buildEntity(request);
        entity.setFlightId(flightId);
        Flight updated = flightService.update(entity);
        FlightResponseDto dto = mapper.toDto(updated);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
            @PathVariable("id") Integer flightId,
            @RequestParam(value = "locale", required = false) Locale locale
    ) {
        Optional<Flight> found = flightService.findById(flightId);
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
            @RequestParam("no") String flightNo,
            @RequestParam(value = "locale", required = false) Locale locale
    ) {
        List<Flight> found = flightService.findByFlightNo(flightNo);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer flightId) {
        Optional<Flight> found = flightService.findById(flightId);
        if (found.isPresent()) {
            flightService.delete(flightId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
