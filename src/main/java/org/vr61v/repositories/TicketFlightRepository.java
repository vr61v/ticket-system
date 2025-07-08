package org.vr61v.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vr61v.entities.TicketFlight;
import org.vr61v.entities.embedded.TicketFlightID;

import java.util.List;

@Repository
public interface TicketFlightRepository extends JpaRepository<TicketFlight, TicketFlightID> {

    @Query("SELECT tf FROM TicketFlight tf WHERE tf.id.ticket.ticketNo = :ticketNo")
    List<TicketFlight> findTicketFlightsByTicketNo(String ticketNo);

    @Modifying
    @Query("DELETE FROM TicketFlight tf WHERE tf.id.ticket.ticketNo = :ticketNo")
    void deleteTicketFlightsByTicketNo(String ticketNo);

}
