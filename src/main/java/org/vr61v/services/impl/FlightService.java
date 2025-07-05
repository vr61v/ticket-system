package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Flight;
import org.vr61v.repositories.FlightRepository;
import org.vr61v.services.CrudService;

import java.util.List;

@Service
public class FlightService extends CrudService<Flight, Integer> {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository repository, FlightRepository flightRepository) {
        super(repository);
        this.flightRepository = flightRepository;
    }

    public List<Flight> findByFlightNo(String flightNo) {
        return flightRepository.findFlightByFlightNo(flightNo);
    }
}
