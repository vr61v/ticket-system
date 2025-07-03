package org.vr61v.dtos.boardingPass;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardingPassRequestDto {

    @NotNull @Positive
    private Integer boardingNo;

    @NotNull @Size(min = 1, max = 4)
    private String seatNo;

}
