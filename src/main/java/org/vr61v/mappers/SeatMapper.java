package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.seat.SeatRequestDto;
import org.vr61v.dtos.seat.SeatResponseDto;
import org.vr61v.entities.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "aircraftCode", source = "entity.id.aircraft.aircraftCode")
    @Mapping(target = "seatNo", source = "entity.id.seatNo")
    SeatResponseDto toDto(Seat entity);

    Seat toEntity(SeatRequestDto dto);

}
