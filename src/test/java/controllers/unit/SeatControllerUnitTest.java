package controllers.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.vr61v.controllers.v1.SeatController;
import org.vr61v.dtos.seat.SeatRequestDto;
import org.vr61v.dtos.seat.SeatResponseDto;
import org.vr61v.entities.Aircraft;
import org.vr61v.entities.Seat;
import org.vr61v.entities.embedded.SeatID;
import org.vr61v.mappers.SeatMapper;
import org.vr61v.services.impl.AircraftService;
import org.vr61v.services.impl.SeatService;
import org.vr61v.types.FareCondition;

import java.util.List;
import java.util.Optional;

import static controllers.unit.util.CommonAssertions.assertErrorResponse;
import static controllers.unit.util.CommonAssertions.assertSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatControllerUnitTest {

    private static final String AIRCRAFT_CODE = "AIRCRAFT";
    private static final String SEAT_NO = "1A";
    private static final String NOT_FOUND_CODE = "XXX";
    private static final FareCondition FARE_CONDITION = FareCondition.BUSINESS;

    private static final Aircraft AIRCRAFT = Aircraft.builder()
            .aircraftCode(AIRCRAFT_CODE)
            .build();

    private static final SeatID SEAT_ID = SeatID.builder()
            .aircraft(AIRCRAFT)
            .seatNo(SEAT_NO)
            .build();

    private static final Seat SEAT = Seat.builder()
            .id(SEAT_ID)
            .fareConditions(FARE_CONDITION)
            .build();

    private static final List<Seat> SEAT_LIST = List.of(SEAT);

    private static final SeatRequestDto REQUEST = SeatRequestDto.builder()
            .fareConditions(FARE_CONDITION)
            .build();

    private static final SeatResponseDto RESPONSE = SeatResponseDto.builder()
            .aircraftCode(AIRCRAFT_CODE)
            .seatNo(SEAT_NO)
            .fareConditions(FARE_CONDITION)
            .build();

    private static final List<SeatResponseDto> RESPONSE_LIST = List.of(RESPONSE);

    @Mock
    private SeatService seatService;

    @Mock
    private AircraftService aircraftService;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private SeatController seatController;

    @Test
    @DisplayName("Create seat - should return 201 CREATED when aircraft exists")
    void create_ShouldReturnCreated_WhenAircraftExists() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(seatService.create(any(Seat.class))).thenReturn(SEAT);
        when(seatMapper.toEntity(any(SeatRequestDto.class))).thenReturn(SEAT);
        when(seatMapper.toDto(SEAT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = seatController.create(AIRCRAFT_CODE, SEAT_NO, REQUEST);

        verify(aircraftService).findById(AIRCRAFT_CODE);
        verify(seatService).create(argThat(seat ->
                seat.getId().getAircraft().equals(AIRCRAFT) &&
                        seat.getId().getSeatNo().equals(SEAT_NO) &&
                        seat.getFareConditions().equals(FARE_CONDITION)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Create seat - should throw 404 when aircraft not found")
    void create_ShouldThrowNotFound_WhenAircraftNotFound() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.empty());
        when(seatMapper.toEntity(any(SeatRequestDto.class))).thenReturn(SEAT);

        assertThatThrownBy(() -> seatController.create(AIRCRAFT_CODE, SEAT_NO, REQUEST))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);

        verify(seatService, never()).create(any());
    }

    @Test
    @DisplayName("Update seat - should return 200 OK when aircraft exists")
    void update_ShouldReturnOk_WhenAircraftExists() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(seatService.update(any(Seat.class))).thenReturn(SEAT);
        when(seatMapper.toEntity(any(SeatRequestDto.class))).thenReturn(SEAT);
        when(seatMapper.toDto(SEAT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = seatController.update(AIRCRAFT_CODE, SEAT_NO, REQUEST);

        verify(seatService).update(argThat(seat ->
                seat.getId().getAircraft().equals(AIRCRAFT) &&
                        seat.getId().getSeatNo().equals(SEAT_NO) &&
                        seat.getFareConditions().equals(FARE_CONDITION)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find seat by ID - should return 200 OK with seat data")
    void findById_ShouldReturnSeat() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(seatService.findById(SEAT_ID)).thenReturn(Optional.of(SEAT));
        when(seatMapper.toDto(SEAT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = seatController.findById(AIRCRAFT_CODE, SEAT_NO);

        verify(seatService).findById(SEAT_ID);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find non-existent seat - should return 404 NOT FOUND")
    void findById_NonExistentSeat_ShouldReturnNotFound() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(seatService.findById(SEAT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = seatController.findById(AIRCRAFT_CODE, SEAT_NO);

        verify(seatService).findById(SEAT_ID);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Find all seats by aircraft code - should return list of seats")
    void findAll_ShouldReturnSeatList() {
        when(seatService.findByAircraftCode(AIRCRAFT_CODE)).thenReturn(SEAT_LIST);
        when(seatMapper.toDto(SEAT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = seatController.findAll(AIRCRAFT_CODE);

        verify(seatService).findByAircraftCode(AIRCRAFT_CODE);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LIST);
    }

    @Test
    @DisplayName("Find all seats for non-existent aircraft - should return 404 NOT FOUND")
    void findAll_NonExistentAircraft_ShouldReturnNotFound() {
        when(seatService.findByAircraftCode(NOT_FOUND_CODE)).thenReturn(List.of());

        ResponseEntity<?> response = seatController.findAll(NOT_FOUND_CODE);

        verify(seatService).findByAircraftCode(NOT_FOUND_CODE);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete seat - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(seatService.findById(SEAT_ID)).thenReturn(Optional.of(SEAT));
        doNothing().when(seatService).delete(SEAT_ID);

        ResponseEntity<?> response = seatController.delete(AIRCRAFT_CODE, SEAT_NO);

        verify(seatService).delete(SEAT_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent seat - should return 404 NOT FOUND")
    void delete_NonExistentSeat_ShouldReturnNotFound() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        when(seatService.findById(SEAT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = seatController.delete(AIRCRAFT_CODE, SEAT_NO);

        verify(seatService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete all seats by aircraft code - should return 204 NO CONTENT")
    void deleteAll_ShouldReturnNoContent() {
        when(aircraftService.findById(AIRCRAFT_CODE)).thenReturn(Optional.of(AIRCRAFT));
        doNothing().when(seatService).deleteByAircraftCode(AIRCRAFT_CODE);

        ResponseEntity<?> response = seatController.deleteAll(AIRCRAFT_CODE);

        verify(seatService).deleteByAircraftCode(AIRCRAFT_CODE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete all seats for non-existent aircraft - should return 404 NOT FOUND")
    void deleteAll_NonExistentAircraft_ShouldReturnNotFound() {
        when(aircraftService.findById(NOT_FOUND_CODE)).thenReturn(Optional.empty());

        ResponseEntity<?> response = seatController.deleteAll(NOT_FOUND_CODE);

        verify(seatService, never()).deleteByAircraftCode(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}