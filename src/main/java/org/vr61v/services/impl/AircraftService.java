package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Aircraft;
import org.vr61v.repositories.AircraftRepository;
import org.vr61v.services.CrudService;

@Service
public class AircraftService extends CrudService<Aircraft, String> {

    public AircraftService(AircraftRepository repository) {
        super(repository);
    }

}
