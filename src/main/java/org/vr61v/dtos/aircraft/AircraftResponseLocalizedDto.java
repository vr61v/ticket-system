package org.vr61v.dtos.aircraft;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AircraftResponseLocalizedDto {

    @Size(min = 3, max = 3)
    private String code;

    @NotNull @Valid
    private String model;

    @NotNull @Positive
    private Integer range;

}
