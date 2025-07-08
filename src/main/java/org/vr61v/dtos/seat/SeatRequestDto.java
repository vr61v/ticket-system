package org.vr61v.dtos.seat;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.vr61v.types.FareCondition;

@Data
@Builder
public class SeatRequestDto {

    @NotNull
    private FareCondition fareConditions;

}
