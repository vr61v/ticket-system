package org.vr61v.dtos.aircraft;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.vr61v.entities.embedded.LocalizedString;

@Data
@Builder
public class AircraftResponseDto {

    @Size(min = 3, max = 3)
    private String code;

    @NotNull @Valid
    private LocalizedString model;

    @NotNull @Positive
    private Integer range;

}
