package org.vr61v.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.vr61v.entities.embedded.LocalizedString;
import org.vr61v.types.Locale;

@Mapper(componentModel = "spring")
public interface LocalizedStringMapper {

    default String localize(LocalizedString string, @Context Locale locale) {
        return locale.equals(Locale.EN) ? string.getEn() : string.getRu();
    }

}
