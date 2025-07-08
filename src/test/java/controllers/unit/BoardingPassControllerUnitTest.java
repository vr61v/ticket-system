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
import org.vr61v.controllers.v1.BoardingPassController;
import org.vr61v.dtos.boardingPass.BoardingPassRequestDto;
import org.vr61v.dtos.boardingPass.BoardingPassResponseDto;
import org.vr61v.entities.BoardingPass;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.entities.embedded.TicketFlightID;
import org.vr61v.mappers.BoardingPassMapper;
import org.vr61v.services.impl.BoardingPassService;
import org.vr61v.services.impl.FlightService;
import org.vr61v.services.impl.TicketService;

import java.util.Optional;

import static controllers.unit.util.CommonAssertions.assertErrorResponse;
import static controllers.unit.util.CommonAssertions.assertSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardingPassControllerUnitTest {

    private static final String TICKET_NO = "0000000000000";
    private static final Integer FLIGHT_ID = 1;
    private static final String NOT_FOUND_TICKET = "NOT_FOUND";
    private static final Integer NOT_FOUND_FLIGHT = -1;
    private static final Integer BOARDING_NO = 1;
    private static final String SEAT_NO = "15A";

    private static final Ticket TICKET = Ticket.builder()
            .ticketNo(TICKET_NO)
            .build();

    private static final Flight FLIGHT = Flight.builder()
            .flightId(FLIGHT_ID)
            .build();

    private static final TicketFlightID TICKET_FLIGHT_ID = TicketFlightID.builder()
            .ticket(TICKET)
            .flight(FLIGHT)
            .build();

    private static final BoardingPass BOARDING_PASS = BoardingPass.builder()
            .id(TICKET_FLIGHT_ID)
            .boardingNo(BOARDING_NO)
            .seatNo(SEAT_NO)
            .build();

    private static final BoardingPassRequestDto REQUEST = BoardingPassRequestDto.builder()
            .boardingNo(BOARDING_NO)
            .seatNo(SEAT_NO)
            .build();

    private static final BoardingPassResponseDto RESPONSE = BoardingPassResponseDto.builder()
            .ticketNo(TICKET_NO)
            .flightId(FLIGHT_ID)
            .boardingNo(BOARDING_NO)
            .seatNo(SEAT_NO)
            .build();

    @Mock
    private BoardingPassService boardingPassService;

    @Mock
    private TicketService ticketService;

    @Mock
    private FlightService flightService;

    @Mock
    private BoardingPassMapper boardingPassMapper;

    @InjectMocks
    private BoardingPassController boardingPassController;

    @Test
    @DisplayName("Create boarding pass - should return 201 CREATED when ticket and flight exist")
    void create_ShouldReturnCreated_WhenTicketAndFlightExist() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(boardingPassService.create(any(BoardingPass.class))).thenReturn(BOARDING_PASS);
        when(boardingPassMapper.toEntity(any(BoardingPassRequestDto.class))).thenReturn(BOARDING_PASS);
        when(boardingPassMapper.toDto(BOARDING_PASS)).thenReturn(RESPONSE);

        ResponseEntity<?> response = boardingPassController.create(TICKET_NO, FLIGHT_ID, REQUEST);

        verify(ticketService).findById(TICKET_NO);
        verify(flightService).findById(FLIGHT_ID);
        verify(boardingPassService).create(argThat(bp ->
                bp.getId().getTicket().equals(TICKET) &&
                        bp.getId().getFlight().equals(FLIGHT) &&
                        bp.getBoardingNo().equals(BOARDING_NO) &&
                        bp.getSeatNo().equals(SEAT_NO)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Create boarding pass - should throw 404 when ticket not found")
    void create_ShouldThrowNotFound_WhenTicketNotFound() {
        when(ticketService.findById(NOT_FOUND_TICKET)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardingPassController.create(NOT_FOUND_TICKET, FLIGHT_ID, REQUEST))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);

        verify(boardingPassService, never()).create(any());
    }

    @Test
    @DisplayName("Create boarding pass - should throw 404 when flight not found")
    void create_ShouldThrowNotFound_WhenFlightNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(NOT_FOUND_FLIGHT)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardingPassController.create(TICKET_NO, NOT_FOUND_FLIGHT, REQUEST))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);

        verify(boardingPassService, never()).create(any());
    }

    @Test
    @DisplayName("Update boarding pass - should return 200 OK when ticket and flight exist")
    void update_ShouldReturnOk_WhenTicketAndFlightExist() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(boardingPassService.update(any(BoardingPass.class))).thenReturn(BOARDING_PASS);
        when(boardingPassMapper.toEntity(any(BoardingPassRequestDto.class))).thenReturn(BOARDING_PASS);
        when(boardingPassMapper.toDto(BOARDING_PASS)).thenReturn(RESPONSE);

        ResponseEntity<?> response = boardingPassController.update(TICKET_NO, FLIGHT_ID, REQUEST);

        verify(boardingPassService).update(argThat(bp ->
                bp.getId().getTicket().equals(TICKET) &&
                        bp.getId().getFlight().equals(FLIGHT) &&
                        bp.getBoardingNo().equals(BOARDING_NO) &&
                        bp.getSeatNo().equals(SEAT_NO)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find boarding pass by ID - should return 200 OK with boarding pass data")
    void findById_ShouldReturnBoardingPass() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(boardingPassService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.of(BOARDING_PASS));
        when(boardingPassMapper.toDto(BOARDING_PASS)).thenReturn(RESPONSE);

        ResponseEntity<?> response = boardingPassController.findById(TICKET_NO, FLIGHT_ID);

        verify(boardingPassService).findById(TICKET_FLIGHT_ID);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find non-existent boarding pass - should return 404 NOT FOUND")
    void findById_NonExistentBoardingPass_ShouldReturnNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(boardingPassService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = boardingPassController.findById(TICKET_NO, FLIGHT_ID);

        verify(boardingPassService).findById(TICKET_FLIGHT_ID);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete boarding pass - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(boardingPassService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.of(BOARDING_PASS));
        doNothing().when(boardingPassService).delete(TICKET_FLIGHT_ID);

        ResponseEntity<?> response = boardingPassController.delete(TICKET_NO, FLIGHT_ID);

        verify(boardingPassService).delete(TICKET_FLIGHT_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent boarding pass - should return 404 NOT FOUND")
    void delete_NonExistentBoardingPass_ShouldReturnNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(boardingPassService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = boardingPassController.delete(TICKET_NO, FLIGHT_ID);

        verify(boardingPassService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}