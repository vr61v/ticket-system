package org.vr61v.dtos.airport;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.vr61v.entities.embedded.LocalizedString;

@Data
public class AirportRequestDto {

    @NotNull @Valid
    private LocalizedString name;

    @NotNull @Valid
    private LocalizedString city;

    @NotNull @NotBlank
    private String timezone;

}
