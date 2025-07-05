package org.vr61v.dtos.ticketFlight;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.vr61v.types.FareCondition;

@Data
public class TicketFlightRequestDto {

    @NotNull
    private FareCondition fareConditions;

    @NotNull @Positive
    private Float amount;

}
