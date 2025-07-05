package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Ticket;
import org.vr61v.repositories.TicketRepository;
import org.vr61v.services.CrudService;

import java.util.List;

@Service
public class TicketService extends CrudService<Ticket, String> {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository repository) {
        super(repository);
        this.ticketRepository = repository;
    }

    public List<Ticket> findByBookRef(String bookRef) {
        return ticketRepository.findTicketByBooking_BookRef(bookRef);
    }

}
