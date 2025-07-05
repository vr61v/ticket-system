package org.vr61v.dtos.flight;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.types.FlightStatus;

import java.time.OffsetDateTime;

@Data
public class FlightRequestDto {

    @Size(min = 6, max = 6)
    private String no;

    @NotNull
    private FlightStatus status;

    @NotNull @Valid
    private String aircraftCode;

    @NotNull @Valid
    private String departureAirportCode;

    @NotNull @Valid
    private String arrivalAirportCode;

    @NotNull
    private OffsetDateTime scheduledDeparture;

    @NotNull
    private OffsetDateTime scheduledArrival;

    private OffsetDateTime actualDeparture;

    private OffsetDateTime actualArrival;


}
