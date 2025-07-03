package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Flight;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    @Query("SELECT f FROM Flight f JOIN FETCH f.aircraft JOIN FETCH f.arrivalAirport JOIN FETCH f.departureAirport")
    List<Flight> findAll();

}
