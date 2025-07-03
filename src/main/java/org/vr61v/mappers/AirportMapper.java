package org.vr61v.mappers;

import org.mapstruct.*;
import org.vr61v.dtos.airport.AirportRequestDto;
import org.vr61v.dtos.airport.AirportResponseDto;
import org.vr61v.dtos.airport.AirportResponseLocalizedDto;
import org.vr61v.entities.Airport;
import org.vr61v.types.Locale;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    @Mapping(target = "code", source = "entity.airportCode")
    @Mapping(target = "name", source = "entity.airportName")
    AirportResponseDto toDto(Airport entity);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "code", source = "entity.airportCode")
    AirportResponseLocalizedDto toLocalizedDto(Airport entity, Locale locale);

    @Mapping(target = "airportName", source = "dto.name")
    Airport toEntity(AirportRequestDto dto);

    @AfterMapping
    default void setLocale(
            @MappingTarget AirportResponseLocalizedDto dto,
            Airport entity,
            Locale locale
    ) {
        if (locale.equals(Locale.EN)) dto.setName(entity.getAirportName().getEn());
        else dto.setName(entity.getAirportName().getRu());

        if (locale.equals(Locale.EN)) dto.setCity(entity.getCity().getEn());
        else dto.setCity(entity.getCity().getRu());

    }

}
