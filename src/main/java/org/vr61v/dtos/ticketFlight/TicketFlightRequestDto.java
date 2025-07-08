package org.vr61v.dtos.ticketFlight;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.vr61v.types.FareCondition;

@Data
@Builder
public class TicketFlightRequestDto {

    @NotNull
    private FareCondition fareConditions;

    @NotNull @Positive
    private Float amount;

}
