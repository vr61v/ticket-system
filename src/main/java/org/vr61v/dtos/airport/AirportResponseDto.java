package org.vr61v.dtos.airport;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.embedded.LocalizedString;

@Data
public class AirportResponseDto {

    @Size(min = 3, max = 3)
    private String code;

    @NotNull @Valid
    private LocalizedString name;

    @NotNull @Valid
    private LocalizedString city;

    @NotNull @NotBlank
    private String timezone;

}
