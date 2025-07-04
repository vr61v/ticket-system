package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.Ticket;
import org.vr61v.repositories.TicketRepository;
import org.vr61v.services.CrudService;

@Service
public class TicketService extends CrudService<Ticket, String> {

    public TicketService(TicketRepository repository) {
        super(repository);
    }

}
