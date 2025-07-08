package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.boardingPass.BoardingPassRequestDto;
import org.vr61v.dtos.boardingPass.BoardingPassResponseDto;
import org.vr61v.entities.BoardingPass;

@Mapper(componentModel = "spring")
public interface BoardingPassMapper {

    @Mapping(target = "ticketNo", source = "entity.id.ticket.ticketNo")
    @Mapping(target = "flightId", source = "entity.id.flight.flightId")
    BoardingPassResponseDto toDto(BoardingPass entity);

    BoardingPass toEntity(BoardingPassRequestDto dto);

}
