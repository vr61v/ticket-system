package org.vr61v.mappers;

import org.mapstruct.*;
import org.vr61v.dtos.aircraft.AircraftRequestDto;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.dtos.aircraft.AircraftResponseLocalizedDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.types.Locale;

@Mapper(
        componentModel = "spring",
        uses = {LocalizedStringMapper.class}
)
public interface AircraftMapper {

    @Mapping(target = "code", source = "entity.aircraftCode")
    AircraftResponseDto toDto(Aircraft entity);

    @Mapping(target = "code", source = "entity.aircraftCode")
    AircraftResponseLocalizedDto toLocalizedDto(Aircraft entity, @Context Locale locale);

    Aircraft toEntity(AircraftRequestDto dto);

}
