package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Airport;
import org.vr61v.repositories.AirportRepository;
import org.vr61v.services.CrudService;

@Service
public class AirportService extends CrudService<Airport, String> {

    public AirportService(AirportRepository repository) {
        super(repository);
    }

}
