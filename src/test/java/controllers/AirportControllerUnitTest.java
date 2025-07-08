package controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vr61v.controllers.v1.AirportController;
import org.vr61v.dtos.airport.AirportRequestDto;
import org.vr61v.dtos.airport.AirportResponseDto;
import org.vr61v.dtos.airport.AirportResponseLocalizedDto;
import org.vr61v.entities.Airport;
import org.vr61v.entities.embedded.LocalizedString;
import org.vr61v.mappers.AirportMapper;
import org.vr61v.services.impl.AirportService;
import org.vr61v.types.Locale;

import java.util.List;
import java.util.Optional;

import static controllers.CommonAssertions.assertErrorResponse;
import static controllers.CommonAssertions.assertSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportControllerUnitTest {

    private static final String CODE = "LED";
    private static final String NOT_FOUND_CODE = "XXX";
    private static final LocalizedString NAME = new LocalizedString("Pulkovo", "Пулково");
    private static final LocalizedString CITY = new LocalizedString("Saint Petersburg", "Санкт-Петербург");
    private static final String TIMEZONE = "Europe/Moscow";

    private static final Airport AIRPORT = Airport.builder()
            .airportCode(CODE)
            .airportName(NAME)
            .city(CITY)
            .timezone(TIMEZONE)
            .build();

    private static final List<Airport> AIRPORT_LIST = List.of(AIRPORT);

    private static final AirportRequestDto REQUEST = AirportRequestDto.builder()
            .name(NAME)
            .city(CITY)
            .timezone(TIMEZONE)
            .build();

    private static final AirportResponseDto RESPONSE = AirportResponseDto.builder()
            .code(CODE)
            .name(NAME)
            .city(CITY)
            .timezone(TIMEZONE)
            .build();

    private static final List<AirportResponseDto> RESPONSE_LIST = List.of(RESPONSE);

    private static final AirportResponseLocalizedDto RESPONSE_LOCALIZED_RU = AirportResponseLocalizedDto.builder()
            .code(CODE)
            .name(NAME.getRu())
            .city(CITY.getRu())
            .timezone(TIMEZONE)
            .build();

    private static final AirportResponseLocalizedDto RESPONSE_LOCALIZED_EN = AirportResponseLocalizedDto.builder()
            .code(CODE)
            .name(NAME.getEn())
            .city(CITY.getEn())
            .timezone(TIMEZONE)
            .build();

    private static final List<AirportResponseLocalizedDto> RESPONSE_LOCALIZED_RU_LIST = List.of(RESPONSE_LOCALIZED_RU);
    private static final List<AirportResponseLocalizedDto> RESPONSE_LOCALIZED_EN_LIST = List.of(RESPONSE_LOCALIZED_EN);

    @Mock
    private AirportService airportService;

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private AirportController airportController;

    @Test
    @DisplayName("Create airport - should return 201 CREATED with response body")
    void create_ShouldReturnCreated() {
        when(airportService.create(any(Airport.class))).thenReturn(AIRPORT);
        when(airportMapper.toDto(AIRPORT)).thenReturn(RESPONSE);
        when(airportMapper.toEntity(any(AirportRequestDto.class))).thenReturn(AIRPORT);

        ResponseEntity<?> response = airportController.create(CODE, REQUEST);

        verify(airportService).create(argThat(airport ->
                airport.getAirportCode().equals(CODE) &&
                        airport.getAirportName().equals(NAME) &&
                        airport.getCity().equals(CITY) &&
                        airport.getTimezone().equals(TIMEZONE)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Update airport - should return 200 OK with updated airport")
    void update_ShouldReturnOk() {
        when(airportService.update(any(Airport.class))).thenReturn(AIRPORT);
        when(airportMapper.toDto(AIRPORT)).thenReturn(RESPONSE);
        when(airportMapper.toEntity(any(AirportRequestDto.class))).thenReturn(AIRPORT);

        ResponseEntity<?> response = airportController.update(CODE, REQUEST);

        verify(airportService).update(argThat(airport ->
                airport.getAirportCode().equals(CODE) &&
                        airport.getAirportName().equals(NAME) &&
                        airport.getCity().equals(CITY) &&
                        airport.getTimezone().equals(TIMEZONE)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find airport by ID - should return 200 OK with airport data")
    void findById_ShouldReturnAirport() {
        when(airportService.findById(CODE)).thenReturn(Optional.of(AIRPORT));
        when(airportMapper.toDto(AIRPORT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = airportController.findById(CODE, null);

        verify(airportService).findById(CODE);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find airport by ID with RU locale - should return localized response")
    void findById_WithRuLocale_ShouldReturnLocalized() {
        when(airportService.findById(CODE)).thenReturn(Optional.of(AIRPORT));
        when(airportMapper.toLocalizedDto(AIRPORT, Locale.RU)).thenReturn(RESPONSE_LOCALIZED_RU);

        ResponseEntity<?> response = airportController.findById(CODE, Locale.RU);

        verify(airportService).findById(CODE);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_RU);
    }

    @Test
    @DisplayName("Find airport by ID with EN locale - should return localized response")
    void findById_WithEnLocale_ShouldReturnLocalized() {
        when(airportService.findById(CODE)).thenReturn(Optional.of(AIRPORT));
        when(airportMapper.toLocalizedDto(AIRPORT, Locale.EN)).thenReturn(RESPONSE_LOCALIZED_EN);

        ResponseEntity<?> response = airportController.findById(CODE, Locale.EN);

        verify(airportService).findById(CODE);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_EN);
    }

    @Test
    @DisplayName("Find non-existent airport - should return 404 NOT FOUND")
    void findById_NonExistentAirport_ShouldReturnNotFound() {
        when(airportService.findById(NOT_FOUND_CODE)).thenReturn(Optional.empty());

        ResponseEntity<?> response = airportController.findById(NOT_FOUND_CODE, null);

        verify(airportService).findById(NOT_FOUND_CODE);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Find all airports - should return list of airports")
    void findAll_ShouldReturnAirportList() {
        when(airportService.findAll()).thenReturn(AIRPORT_LIST);
        when(airportMapper.toDto(AIRPORT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = airportController.findAll(null);

        verify(airportService).findAll();
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LIST);
    }

    @Test
    @DisplayName("Find all airports with RU locale - should return localized list")
    void findAll_WithRuLocale_ShouldReturnLocalizedList() {
        when(airportService.findAll()).thenReturn(AIRPORT_LIST);
        when(airportMapper.toLocalizedDto(AIRPORT, Locale.RU)).thenReturn(RESPONSE_LOCALIZED_RU);

        ResponseEntity<?> response = airportController.findAll(Locale.RU);

        verify(airportService).findAll();
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_RU_LIST);
    }

    @Test
    @DisplayName("Find all airports with EN locale - should return localized list")
    void findAll_WithEnLocale_ShouldReturnLocalizedList() {
        when(airportService.findAll()).thenReturn(AIRPORT_LIST);
        when(airportMapper.toLocalizedDto(AIRPORT, Locale.EN)).thenReturn(RESPONSE_LOCALIZED_EN);

        ResponseEntity<?> response = airportController.findAll(Locale.EN);

        verify(airportService).findAll();
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_EN_LIST);
    }

    @Test
    @DisplayName("Delete airport - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(airportService.findById(CODE)).thenReturn(Optional.of(AIRPORT));
        doNothing().when(airportService).delete(CODE);

        ResponseEntity<?> response = airportController.delete(CODE);

        verify(airportService).delete(CODE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent airport - should return 404 NOT FOUND")
    void delete_NonExistentAirport_ShouldReturnNotFound() {
        when(airportService.findById(NOT_FOUND_CODE)).thenReturn(Optional.empty());

        ResponseEntity<?> response = airportController.delete(NOT_FOUND_CODE);

        verify(airportService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}