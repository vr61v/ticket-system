package org.vr61v.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.flight.FlightRequestDto;
import org.vr61v.dtos.flight.FlightResponseDto;
import org.vr61v.dtos.flight.FlightResponseLocalizedDto;
import org.vr61v.entities.Flight;
import org.vr61v.types.Locale;

@Mapper(
        componentModel = "spring",
        uses = {AircraftMapper.class, AirportMapper.class}
)
public interface FlightMapper {

    @Mapping(target = "id", source = "entity.flightId")
    @Mapping(target = "no", source = "entity.flightNo")
    FlightResponseDto toDto(Flight entity);

    @Mapping(target = "id", source = "entity.flightId")
    @Mapping(target = "no", source = "entity.flightNo")
    FlightResponseLocalizedDto toLocalizedDto(Flight entity, @Context Locale locale);

    @Mapping(target = "flightNo", source = "dto.no")
    Flight toEntity(FlightRequestDto dto);

}