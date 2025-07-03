package org.vr61v.dtos.aircraft;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.vr61v.embedded.LocalizedString;

@Data
public class AircraftRequestDto {

    @NotNull @Valid
    private LocalizedString model;

    @NotNull @Positive
    private Integer range;

}
