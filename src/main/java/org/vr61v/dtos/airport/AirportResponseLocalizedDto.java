package org.vr61v.dtos.airport;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirportResponseLocalizedDto {

    @Size(min = 3, max = 3)
    private String code;

    @NotNull @Valid
    private String name;

    @NotNull @Valid
    private String city;

    @NotNull @NotBlank
    private String timezone;

}
