package controllers.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vr61v.controllers.v1.AircraftController;
import org.vr61v.dtos.aircraft.AircraftRequestDto;
import org.vr61v.dtos.aircraft.AircraftResponseDto;
import org.vr61v.dtos.aircraft.AircraftResponseLocalizedDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.entities.embedded.LocalizedString;
import org.vr61v.mappers.AircraftMapper;
import org.vr61v.services.impl.AircraftService;
import org.vr61v.types.Locale;

import java.util.List;
import java.util.Optional;

import static controllers.unit.CommonAssertions.assertErrorResponse;
import static controllers.unit.CommonAssertions.assertSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftControllerUnitTest {

    private static final String CODE = "000";
    private static final String NOT_FOUND_CODE = "NOT_FOUND";
    private static final LocalizedString MODEL = new LocalizedString("model", "модель");
    private static final Integer RANGE = 5000;

    private static final Aircraft AIRCRAFT = Aircraft.builder()
            .aircraftCode(CODE)
            .model(MODEL)
            .range(RANGE)
            .build();

    private static final List<Aircraft> AIRCRAFT_LIST = List.of(AIRCRAFT);

    private static final AircraftRequestDto REQUEST = AircraftRequestDto.builder()
            .model(MODEL)
            .range(RANGE)
            .build();

    private static final AircraftResponseDto RESPONSE = AircraftResponseDto.builder()
            .code(CODE)
            .model(MODEL)
            .range(RANGE)
            .build();

    private static final List<AircraftResponseDto> RESPONSE_LIST = List.of(RESPONSE);

    private static final AircraftResponseLocalizedDto RESPONSE_LOCALIZED_RU = AircraftResponseLocalizedDto.builder()
            .code(CODE)
            .model(MODEL.getRu())
            .range(RANGE)
            .build();

    private static final AircraftResponseLocalizedDto RESPONSE_LOCALIZED_EN = AircraftResponseLocalizedDto.builder()
            .code(CODE)
            .model(MODEL.getEn())
            .range(RANGE)
            .build();

    private static final List<AircraftResponseLocalizedDto> RESPONSE_LOCALIZED_RU_LIST = List.of(RESPONSE_LOCALIZED_RU);

    private static final List<AircraftResponseLocalizedDto> RESPONSE_LOCALIZED_EN_LIST = List.of(RESPONSE_LOCALIZED_EN);

    @Mock
    private AircraftService aircraftService;

    @Mock
    private AircraftMapper aircraftMapper;

    @InjectMocks
    private AircraftController aircraftController;

    @Test
    @DisplayName("Create aircraft - should return 201 CREATED with response body")
    void create_ShouldReturnCreated() {
        when(aircraftService.create(any())).thenReturn(AIRCRAFT);
        when(aircraftMapper.toEntity(any())).thenReturn(AIRCRAFT);
        when(aircraftMapper.toDto(AIRCRAFT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = aircraftController.create(CODE, REQUEST);

        verify(aircraftService).create(argThat(aircraft ->
                aircraft.getAircraftCode().equals(CODE) &&
                        aircraft.getModel().equals(MODEL) &&
                        aircraft.getRange().equals(RANGE)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Update aircraft - should return 200 OK with updated aircraft")
    void update_ShouldReturnOk() {
        when(aircraftService.update(any())).thenReturn(AIRCRAFT);
        when(aircraftMapper.toEntity(any())).thenReturn(AIRCRAFT);
        when(aircraftMapper.toDto(AIRCRAFT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = aircraftController.update(CODE, REQUEST);

        verify(aircraftService).update(argThat(aircraft ->
                aircraft.getAircraftCode().equals(CODE) &&
                        aircraft.getModel().equals(MODEL) &&
                        aircraft.getRange().equals(RANGE)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find aircraft by ID - should return 200 OK with aircraft data")
    void findById_ShouldReturnAircraft() {
        when(aircraftService.findById(CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(aircraftMapper.toDto(AIRCRAFT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = aircraftController.findById(CODE, null);

        verify(aircraftService).findById(CODE);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find aircraft by ID with RU locale - should return localized response")
    void findById_WithRuLocale_ShouldReturnLocalized() {
        when(aircraftService.findById(CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(aircraftMapper.toLocalizedDto(AIRCRAFT, Locale.RU)).thenReturn(RESPONSE_LOCALIZED_RU);

        ResponseEntity<?> response = aircraftController.findById(CODE, Locale.RU);

        verify(aircraftService).findById(CODE);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_RU);
    }

    @Test
    @DisplayName("Find aircraft by ID with EN locale - should return localized response")
    void findById_WithEnLocale_ShouldReturnLocalized() {
        when(aircraftService.findById(CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(aircraftMapper.toLocalizedDto(AIRCRAFT, Locale.EN)).thenReturn(RESPONSE_LOCALIZED_EN);

        ResponseEntity<?> response = aircraftController.findById(CODE, Locale.EN);

        verify(aircraftService).findById(CODE);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_EN);
    }

    @Test
    @DisplayName("Find non-existent aircraft - should return 404 NOT FOUND")
    void findById_NonExistentAircraft_ShouldReturnNotFound() {
        when(aircraftService.findById(NOT_FOUND_CODE)).thenReturn(Optional.empty());

        ResponseEntity<?> response = aircraftController.findById(NOT_FOUND_CODE, null);

        verify(aircraftService).findById(NOT_FOUND_CODE);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Find all aircraft - should return list of aircraft")
    void findAll_ShouldReturnAircraftList() {
        when(aircraftService.findAll()).thenReturn(AIRCRAFT_LIST);
        when(aircraftMapper.toDto(AIRCRAFT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = aircraftController.findAll(null);

        verify(aircraftService).findAll();
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LIST);
    }

    @Test
    @DisplayName("Find all aircraft with RU locale - should return localized list")
    void findAll_WithRuLocale_ShouldReturnLocalizedList() {
        when(aircraftService.findAll()).thenReturn(AIRCRAFT_LIST);
        when(aircraftMapper.toLocalizedDto(AIRCRAFT, Locale.RU)).thenReturn(RESPONSE_LOCALIZED_RU);

        ResponseEntity<?> response = aircraftController.findAll(Locale.RU);

        verify(aircraftService).findAll();
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_RU_LIST);
    }

    @Test
    @DisplayName("Find all aircraft with EN locale - should return localized list")
    void findAll_WithEnLocale_ShouldReturnLocalizedList() {
        when(aircraftService.findAll()).thenReturn(AIRCRAFT_LIST);
        when(aircraftMapper.toLocalizedDto(AIRCRAFT, Locale.EN)).thenReturn(RESPONSE_LOCALIZED_EN);

        ResponseEntity<?> response = aircraftController.findAll(Locale.EN);

        verify(aircraftService).findAll();
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LOCALIZED_EN_LIST);
    }

    @Test
    @DisplayName("Delete aircraft - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(aircraftService.findById(CODE)).thenReturn(Optional.of(AIRCRAFT));
        doNothing().when(aircraftService).delete(CODE);

        ResponseEntity<?> response = aircraftController.delete(CODE);

        verify(aircraftService).delete(CODE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent aircraft - should return 404 NOT FOUND")
    void delete_NonExistentAircraft_ShouldReturnNotFound() {
        when(aircraftService.findById(NOT_FOUND_CODE)).thenReturn(Optional.empty());

        ResponseEntity<?> response = aircraftController.delete(NOT_FOUND_CODE);

        verify(aircraftService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}