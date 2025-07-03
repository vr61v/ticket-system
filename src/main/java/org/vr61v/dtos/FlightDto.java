package org.vr61v.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.types.FlightStatus;

import java.time.OffsetDateTime;

@Data
public class FlightDto {

    @Positive
    private Integer flightId;

    @Size(min = 6, max = 6)
    private String flightNo;

    @NotNull
    private FlightStatus status;

    @NotNull @Valid
    private AircraftResponseDto aircraft;

    @NotNull @Valid
    private AircraftResponseDto departureAirport;

    @NotNull @Valid
    private AircraftResponseDto arrivalAirport;

    @NotNull
    private OffsetDateTime scheduledDeparture;

    @NotNull
    private OffsetDateTime scheduledArrival;

    private OffsetDateTime actualDeparture;

    private OffsetDateTime actualArrival;

}

