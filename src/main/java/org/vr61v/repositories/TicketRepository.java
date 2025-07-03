package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    @Query("SELECT t FROM Ticket t JOIN FETCH t.booking")
    List<Ticket> findAll();

}
