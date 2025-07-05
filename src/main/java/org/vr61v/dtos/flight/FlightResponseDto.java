package org.vr61v.dtos.flight;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.dtos.airport.AirportResponseDto;
import org.vr61v.types.FlightStatus;

import java.time.OffsetDateTime;

@Data
public class FlightResponseDto {

    @Positive
    private Integer id;

    @Size(min = 6, max = 6)
    private String no;

    @NotNull
    private FlightStatus status;

    @NotNull @Valid
    private AircraftResponseDto aircraft;

    @NotNull @Valid
    private AirportResponseDto departureAirport;

    @NotNull @Valid
    private AirportResponseDto arrivalAirport;

    @NotNull
    private OffsetDateTime scheduledDeparture;

    @NotNull
    private OffsetDateTime scheduledArrival;

    private OffsetDateTime actualDeparture;

    private OffsetDateTime actualArrival;

}
