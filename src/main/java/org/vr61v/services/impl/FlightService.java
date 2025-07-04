package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Flight;
import org.vr61v.repositories.FlightRepository;
import org.vr61v.services.CrudService;

@Service
public class FlightService extends CrudService<Flight, Integer> {

    public FlightService(FlightRepository repository) {
        super(repository);
    }

}
