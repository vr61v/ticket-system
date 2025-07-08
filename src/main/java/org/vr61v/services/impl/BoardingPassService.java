package org.vr61v.services.impl;

import org.springframework.stereotype.Service;
import org.vr61v.entities.BoardingPass;
import org.vr61v.entities.embedded.TicketFlightID;
import org.vr61v.repositories.BoardingPassRepository;
import org.vr61v.services.CrudService;

@Service
public class BoardingPassService extends CrudService<BoardingPass, TicketFlightID> {

    public BoardingPassService(BoardingPassRepository repository) {
        super(repository);
    }

}
