package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.embedded.SeatID;
import org.vr61v.entities.Seat;
import org.vr61v.repositories.SeatRepository;
import org.vr61v.services.CrudService;

import java.util.List;

@Service
public class SeatService extends CrudService<Seat, SeatID> {

    private final SeatRepository repository;

    public SeatService(SeatRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Seat> findSeatsByAircraftCode(String aircraftCode) {
        return repository.findSeatsByAircraftCode(aircraftCode);
    }

    public void deleteSeatsByAircraftCode(String aircraftCode) {
        repository.deleteSeatsByAircraftCode(aircraftCode);
    }

}
