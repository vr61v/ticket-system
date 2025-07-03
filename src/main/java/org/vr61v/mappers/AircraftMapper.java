package org.vr61v.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.vr61v.dtos.aircraft.AircraftRequestDto;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.dtos.aircraft.AircraftResponseLocalizedDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.types.Locale;

@Mapper(componentModel = "spring")
public interface AircraftMapper {

    @Mapping(target = "code", source = "entity.aircraftCode")
    AircraftResponseDto toDto(Aircraft entity);

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "code", source = "entity.aircraftCode")
    AircraftResponseLocalizedDto toLocalizedDto(Aircraft entity, Locale locale);

    Aircraft toEntity(AircraftRequestDto dto);

    @AfterMapping
    default void setLocale(
            @MappingTarget AircraftResponseLocalizedDto dto,
            Aircraft entity,
            Locale locale
    ) {
        if (locale.equals(Locale.EN)) {
            dto.setModel(entity.getModel().getEn());
        } else {
            dto.setModel(entity.getModel().getRu());
        }
    }

}
