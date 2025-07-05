package org.vr61v.dtos.flight;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.dtos.aircraft.AircraftResponseLocalizedDto;
import org.vr61v.dtos.airport.AirportResponseLocalizedDto;
import org.vr61v.types.FlightStatus;

import java.time.OffsetDateTime;

@Data
public class FlightResponseLocalizedDto {

    @Positive
    private Integer id;

    @Size(min = 6, max = 6)
    private String no;

    @NotNull
    private FlightStatus status;

    @NotNull @Valid
    private AircraftResponseLocalizedDto aircraft;

    @NotNull @Valid
    private AirportResponseLocalizedDto departureAirport;

    @NotNull @Valid
    private AirportResponseLocalizedDto arrivalAirport;

    @NotNull
    private OffsetDateTime scheduledDeparture;

    @NotNull
    private OffsetDateTime scheduledArrival;

    private OffsetDateTime actualDeparture;

    private OffsetDateTime actualArrival;

}
