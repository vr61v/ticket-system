package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.vr61v.controllers.v1.FlightController;
import org.vr61v.dtos.flight.FlightRequestDto;
import org.vr61v.dtos.flight.FlightResponseDto;
import org.vr61v.dtos.flight.FlightResponseLocalizedDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.entities.Airport;
import org.vr61v.entities.Flight;
import org.vr61v.mappers.FlightMapper;
import org.vr61v.services.impl.AircraftService;
import org.vr61v.services.impl.AirportService;
import org.vr61v.services.impl.FlightService;
import org.vr61v.types.FlightStatus;
import org.vr61v.types.Locale;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static controllers.CommonAssertions.assertErrorResponse;
import static controllers.CommonAssertions.assertSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightControllerUnitTest {

    private static final Integer FLIGHT_ID = 1;
    private static final String FLIGHT_NO = "000000";
    private static final String AIRCRAFT_CODE = "AIRCRAFT";
    private static final String DEPARTURE_AIRPORT_CODE = "LED";
    private static final String ARRIVAL_AIRPORT_CODE = "SVO";
    private static final OffsetDateTime SCHEDULED_DEPARTURE = OffsetDateTime.now();
    private static final OffsetDateTime SCHEDULED_ARRIVAL = SCHEDULED_DEPARTURE.plusHours(2);
    private static final FlightStatus STATUS = FlightStatus.SCHEDULED;

    private static final Aircraft AIRCRAFT = Aircraft.builder()
            .aircraftCode(AIRCRAFT_CODE)
            .build();

    private static final Airport DEPARTURE_AIRPORT = Airport.builder()
            .airportCode(DEPARTURE_AIRPORT_CODE)
            .build();

    private static final Airport ARRIVAL_AIRPORT = Airport.builder()
            .airportCode(ARRIVAL_AIRPORT_CODE)
            .build();

    private static final Flight FLIGHT = Flight.builder()
            .flightId(FLIGHT_ID)
            .flightNo(FLIGHT_NO)
            .status(STATUS)
            .aircraft(AIRCRAFT)
            .departureAirport(DEPARTURE_AIRPORT)
            .arrivalAirport(ARRIVAL_AIRPORT)
            .scheduledDeparture(SCHEDULED_DEPARTURE)
            .scheduledArrival(SCHEDULED_ARRIVAL)
            .build();

    private static final List<Flight> FLIGHT_LIST = List.of(FLIGHT);

    private static final FlightRequestDto REQUEST = FlightRequestDto.builder()
            .no(FLIGHT_NO)
            .status(STATUS)
            .aircraftCode(AIRCRAFT_CODE)
            .departureAirportCode(DEPARTURE_AIRPORT_CODE)
            .arrivalAirportCode(ARRIVAL_AIRPORT_CODE)
            .scheduledDeparture(SCHEDULED_DEPARTURE)
            .scheduledArrival(SCHEDULED_ARRIVAL)
            .build();

    private static final FlightResponseDto RESPONSE = FlightResponseDto.builder()
            .id(FLIGHT_ID)
            .no(FLIGHT_NO)
            .status(STATUS)
            .aircraft(null)
            .departureAirport(null)
            .arrivalAirport(null)
            .scheduledDeparture(SCHEDULED_DEPARTURE)
            .scheduledArrival(SCHEDULED_ARRIVAL)
            .build();

    private static final FlightResponseLocalizedDto RESPONSE_LOCALIZED = FlightResponseLocalizedDto.builder()
            .id(FLIGHT_ID)
            .no(FLIGHT_NO)
            .status(STATUS)
            .aircraft(null)
            .departureAirport(null)
            .arrivalAirport(null)
            .scheduledDeparture(SCHEDULED_DEPARTURE)
            .scheduledArrival(SCHEDULED_ARRIVAL)
            .build();

    private static final List<FlightResponseDto> RESPONSE_LIST = List.of(RESPONSE);
    private static final List<FlightResponseLocalizedDto> RESPONSE_LOCALIZED_LIST = List.of(RESPONSE_LOCALIZED);

    @Mock
    private AircraftService aircraftService;

    @Mock
    private AirportService airportService;

    @Mock
    private FlightService flightService;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Create flight - should return 201 CREATED when all dependencies exist")
    void create_ShouldReturnCreated_WhenAllDependenciesExist() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(airportService.findById(DEPARTURE_AIRPORT_CODE)).thenReturn(Optional.of(DEPARTURE_AIRPORT));
        when(airportService.findById(ARRIVAL_AIRPORT_CODE)).thenReturn(Optional.of(ARRIVAL_AIRPORT));
        when(flightService.create(any(Flight.class))).thenReturn(FLIGHT);
        when(flightMapper.toEntity(any(FlightRequestDto.class))).thenReturn(FLIGHT);
        when(flightMapper.toDto(FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = flightController.create(REQUEST);

        verify(aircraftService).findById(AIRCRAFT_CODE);
        verify(airportService, times(2)).findById(any());
        verify(flightService).create(argThat(flight ->
                flight.getFlightNo().equals(FLIGHT_NO) &&
                        flight.getStatus().equals(STATUS) &&
                        flight.getAircraft().equals(AIRCRAFT) &&
                        flight.getDepartureAirport().equals(DEPARTURE_AIRPORT) &&
                        flight.getArrivalAirport().equals(ARRIVAL_AIRPORT)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Create flight - should throw 404 when aircraft not found")
    void create_ShouldThrowNotFound_WhenAircraftNotFound() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.empty());
        when(flightMapper.toEntity(any(FlightRequestDto.class))).thenReturn(FLIGHT);

        assertThatThrownBy(() -> flightController.create(REQUEST))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);

        verify(flightService, never()).create(any());
    }

    @Test
    @DisplayName("Update flight - should return 200 OK when all dependencies exist")
    void update_ShouldReturnOk_WhenAllDependenciesExist() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(airportService.findById(DEPARTURE_AIRPORT_CODE)).thenReturn(Optional.of(DEPARTURE_AIRPORT));
        when(airportService.findById(ARRIVAL_AIRPORT_CODE)).thenReturn(Optional.of(ARRIVAL_AIRPORT));
        when(flightService.update(any(Flight.class))).thenReturn(FLIGHT);
        when(flightMapper.toEntity(any(FlightRequestDto.class))).thenReturn(FLIGHT);
        when(flightMapper.toDto(FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = flightController.update(FLIGHT_ID, REQUEST);

        verify(flightService).update(argThat(flight ->
                flight.getFlightId().equals(FLIGHT_ID) &&
                        flight.getFlightNo().equals(FLIGHT_NO) &&
                        flight.getStatus().equals(STATUS) &&
                        flight.getAircraft().equals(AIRCRAFT) &&
                        flight.getDepartureAirport().equals(DEPARTURE_AIRPORT) &&
                        flight.getArrivalAirport().equals(ARRIVAL_AIRPORT)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find flight by ID - should return 200 OK with flight data")
    void findById_ShouldReturnFlight() {
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(flightMapper.toDto(FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = flightController.findById(FLIGHT_ID, null);

        verify(flightService).findById(FLIGHT_ID);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find flight by ID with RU locale - should return localized response")
    void findById_WithRuLocale_ShouldReturnLocalized() {
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(flightMapper.toLocalizedDto(FLIGHT, Locale.RU)).thenReturn(RESPONSE_LOCALIZED);

        ResponseEntity<?> response = flightController.findById(FLIGHT_ID, Locale.RU);

        verify(flightService).findById(FLIGHT_ID);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED);
    }

    @Test
    @DisplayName("Find non-existent flight - should return 404 NOT FOUND")
    void findById_NonExistentFlight_ShouldReturnNotFound() {
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = flightController.findById(FLIGHT_ID, null);

        verify(flightService).findById(FLIGHT_ID);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Find all flights by flight number - should return list of flights")
    void findAll_ShouldReturnFlightList() {
        when(flightService.findByFlightNo(FLIGHT_NO)).thenReturn(FLIGHT_LIST);
        when(flightMapper.toDto(FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = flightController.findAll(FLIGHT_NO, null);

        verify(flightService).findByFlightNo(FLIGHT_NO);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LIST);
    }

    @Test
    @DisplayName("Find all flights by flight number with RU locale - should return localized list")
    void findAll_WithRuLocale_ShouldReturnLocalizedList() {
        when(flightService.findByFlightNo(FLIGHT_NO)).thenReturn(FLIGHT_LIST);
        when(flightMapper.toLocalizedDto(FLIGHT, Locale.RU)).thenReturn(RESPONSE_LOCALIZED);

        ResponseEntity<?> response = flightController.findAll(FLIGHT_NO, Locale.RU);

        verify(flightService).findByFlightNo(FLIGHT_NO);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_LIST);
    }

    @Test
    @DisplayName("Delete flight - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        doNothing().when(flightService).delete(FLIGHT_ID);

        ResponseEntity<?> response = flightController.delete(FLIGHT_ID);

        verify(flightService).delete(FLIGHT_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent flight - should return 404 NOT FOUND")
    void delete_NonExistentFlight_ShouldReturnNotFound() {
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = flightController.delete(FLIGHT_ID);

        verify(flightService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}