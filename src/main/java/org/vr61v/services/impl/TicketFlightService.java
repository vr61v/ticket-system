package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.embedded.TicketFlightID;
import org.vr61v.entities.TicketFlight;
import org.vr61v.repositories.TicketFlightRepository;
import org.vr61v.services.CrudService;

import java.util.List;

@Service
public class TicketFlightService extends CrudService<TicketFlight, TicketFlightID> {

    private final TicketFlightRepository ticketFlightRepository;

    public TicketFlightService(TicketFlightRepository repository, TicketFlightRepository ticketFlightRepository) {
        super(repository);
        this.ticketFlightRepository = ticketFlightRepository;
    }

    public List<TicketFlight> findByTicketNo(String ticketNo) {
        return ticketFlightRepository.findTicketFlightsByTicketNo(ticketNo);
    }

}
