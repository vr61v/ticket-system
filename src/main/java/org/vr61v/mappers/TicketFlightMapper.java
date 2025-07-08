package org.vr61v.mappers;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.ticketFlight.TicketFlightRequestDto;
import org.vr61v.dtos.ticketFlight.TicketFlightResponseDto;
import org.vr61v.entities.TicketFlight;

@Mapper(componentModel = "spring")
public interface TicketFlightMapper {

    @Mapping(target = "ticketNo", source = "entity.id.ticket.ticketNo")
    @Mapping(target = "flightId", source = "entity.id.flight.flightId")
    TicketFlightResponseDto toDto(TicketFlight entity);

    @InheritInverseConfiguration
    TicketFlight toEntity(TicketFlightRequestDto dto);

}
