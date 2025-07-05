package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.TicketDto;
import org.vr61v.dtos.ticket.TicketRequestDto;
import org.vr61v.dtos.ticket.TicketResponseDto;
import org.vr61v.entities.Ticket;

@Mapper(
        componentModel = "spring",
        uses = BookingMapper.class
)
public interface TicketMapper {

    TicketResponseDto toDto(Ticket entity);

    Ticket toEntity(TicketRequestDto dto);

}
