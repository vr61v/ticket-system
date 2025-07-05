package org.vr61v.mappers;

import org.mapstruct.*;
import org.vr61v.dtos.airport.AirportRequestDto;
import org.vr61v.dtos.airport.AirportResponseDto;
import org.vr61v.dtos.airport.AirportResponseLocalizedDto;
import org.vr61v.entities.Airport;
import org.vr61v.types.Locale;

@Mapper(
        componentModel = "spring",
        uses = {LocalizedStringMapper.class}
)
public interface AirportMapper {

    @Mapping(target = "code", source = "entity.airportCode")
    @Mapping(target = "name", source = "entity.airportName")
    AirportResponseDto toDto(Airport entity);

    @Mapping(target = "code", source = "entity.airportCode")
    @Mapping(target = "name", source = "entity.airportName")
    AirportResponseLocalizedDto toLocalizedDto(Airport entity, @Context Locale locale);

    @Mapping(target = "airportName", source = "dto.name")
    Airport toEntity(AirportRequestDto dto);

}
