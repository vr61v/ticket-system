package org.vr61v.services.custom;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.vr61v.entities.TicketFlight;
import org.vr61v.repositories.TicketFlightRepository;

import java.util.List;

@Service
@Transactional
public class TicketFlightCustomService {

    private final TicketFlightRepository ticketFlightRepository;

    public TicketFlightCustomService(TicketFlightRepository ticketFlightRepository) {
        this.ticketFlightRepository = ticketFlightRepository;
    }

    public List<TicketFlight> findTicketFlightsByTicketNo(String ticketNo) {
        return ticketFlightRepository.findTicketFlightsByTicketNo(ticketNo);
    }

    public void deleteTicketFlightsByTicketNo(String ticketNo) {
        ticketFlightRepository.deleteTicketFlightsByTicketNo(ticketNo);
    }

}
