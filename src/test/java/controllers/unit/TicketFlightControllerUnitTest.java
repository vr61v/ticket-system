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
import org.vr61v.controllers.v1.TicketFlightController;
import org.vr61v.dtos.ticketFlight.TicketFlightRequestDto;
import org.vr61v.dtos.ticketFlight.TicketFlightResponseDto;
import org.vr61v.entities.Flight;
import org.vr61v.entities.Ticket;
import org.vr61v.entities.TicketFlight;
import org.vr61v.entities.embedded.TicketFlightID;
import org.vr61v.mappers.TicketFlightMapper;
import org.vr61v.services.impl.FlightService;
import org.vr61v.services.impl.TicketFlightService;
import org.vr61v.services.impl.TicketService;
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
class TicketFlightControllerUnitTest {

    private static final String TICKET_NO = "0000000000000";
    private static final Integer FLIGHT_ID = 1;
    private static final String NOT_FOUND_TICKET = "NOT_FOUND";
    private static final Integer NOT_FOUND_FLIGHT = -1;
    private static final FareCondition FARE_CONDITION = FareCondition.ECONOMY;
    private static final Float AMOUNT = 15000.0f;

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

    private static final TicketFlight TICKET_FLIGHT = TicketFlight.builder()
            .id(TICKET_FLIGHT_ID)
            .fareConditions(FARE_CONDITION)
            .amount(AMOUNT)
            .build();

    private static final List<TicketFlight> TICKET_FLIGHT_LIST = List.of(TICKET_FLIGHT);

    private static final TicketFlightRequestDto REQUEST = TicketFlightRequestDto.builder()
            .fareConditions(FARE_CONDITION)
            .amount(AMOUNT)
            .build();

    private static final TicketFlightResponseDto RESPONSE = TicketFlightResponseDto.builder()
            .ticketNo(TICKET_NO)
            .flightId(FLIGHT_ID)
            .fareConditions(FARE_CONDITION)
            .amount(AMOUNT)
            .build();

    private static final List<TicketFlightResponseDto> RESPONSE_LIST = List.of(RESPONSE);

    @Mock
    private TicketService ticketService;

    @Mock
    private FlightService flightService;

    @Mock
    private TicketFlightService ticketFlightService;

    @Mock
    private TicketFlightMapper ticketFlightMapper;

    @InjectMocks
    private TicketFlightController ticketFlightController;

    @Test
    @DisplayName("Create ticket flight - should return 201 CREATED when ticket and flight exist")
    void create_ShouldReturnCreated_WhenTicketAndFlightExist() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(ticketFlightService.create(any(TicketFlight.class))).thenReturn(TICKET_FLIGHT);
        when(ticketFlightMapper.toEntity(any(TicketFlightRequestDto.class))).thenReturn(TICKET_FLIGHT);
        when(ticketFlightMapper.toDto(TICKET_FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketFlightController.create(TICKET_NO, FLIGHT_ID, REQUEST);

        verify(ticketService).findById(TICKET_NO);
        verify(flightService).findById(FLIGHT_ID);
        verify(ticketFlightService).create(argThat(tf ->
                tf.getId().getTicket().equals(TICKET) &&
                        tf.getId().getFlight().equals(FLIGHT) &&
                        tf.getFareConditions().equals(FARE_CONDITION) &&
                        tf.getAmount().equals(AMOUNT)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Create ticket flight - should throw 404 when ticket not found")
    void create_ShouldThrowNotFound_WhenTicketNotFound() {
        when(ticketService.findById(NOT_FOUND_TICKET)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketFlightController.create(NOT_FOUND_TICKET, FLIGHT_ID, REQUEST))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);

        verify(ticketFlightService, never()).create(any());
    }

    @Test
    @DisplayName("Create ticket flight - should throw 404 when flight not found")
    void create_ShouldThrowNotFound_WhenFlightNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(NOT_FOUND_FLIGHT)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketFlightController.create(TICKET_NO, NOT_FOUND_FLIGHT, REQUEST))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);

        verify(ticketFlightService, never()).create(any());
    }

    @Test
    @DisplayName("Update ticket flight - should return 200 OK when ticket and flight exist")
    void update_ShouldReturnOk_WhenTicketAndFlightExist() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(ticketFlightService.update(any(TicketFlight.class))).thenReturn(TICKET_FLIGHT);
        when(ticketFlightMapper.toEntity(any(TicketFlightRequestDto.class))).thenReturn(TICKET_FLIGHT);
        when(ticketFlightMapper.toDto(TICKET_FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketFlightController.update(TICKET_NO, FLIGHT_ID, REQUEST);

        verify(ticketFlightService).update(argThat(tf ->
                tf.getId().getTicket().equals(TICKET) &&
                        tf.getId().getFlight().equals(FLIGHT) &&
                        tf.getFareConditions().equals(FARE_CONDITION) &&
                        tf.getAmount().equals(AMOUNT)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find ticket flight by ID - should return 200 OK with ticket flight data")
    void findById_ShouldReturnTicketFlight() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(ticketFlightService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.of(TICKET_FLIGHT));
        when(ticketFlightMapper.toDto(TICKET_FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketFlightController.findById(TICKET_NO, FLIGHT_ID);

        verify(ticketFlightService).findById(TICKET_FLIGHT_ID);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find non-existent ticket flight - should return 404 NOT FOUND")
    void findById_NonExistentTicketFlight_ShouldReturnNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(ticketFlightService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = ticketFlightController.findById(TICKET_NO, FLIGHT_ID);

        verify(ticketFlightService).findById(TICKET_FLIGHT_ID);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Find all ticket flights by ticket number - should return list of ticket flights")
    void findAll_ShouldReturnTicketFlightList() {
        when(ticketFlightService.findByTicketNo(TICKET_NO)).thenReturn(TICKET_FLIGHT_LIST);
        when(ticketFlightMapper.toDto(TICKET_FLIGHT)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketFlightController.findAll(TICKET_NO);

        verify(ticketFlightService).findByTicketNo(TICKET_NO);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LIST);
    }

    @Test
    @DisplayName("Find all ticket flights for non-existent ticket - should return empty list")
    void findAll_NonExistentTicket_ShouldReturnEmptyList() {
        when(ticketFlightService.findByTicketNo(NOT_FOUND_TICKET)).thenReturn(List.of());

        ResponseEntity<?> response = ticketFlightController.findAll(NOT_FOUND_TICKET);

        verify(ticketFlightService).findByTicketNo(NOT_FOUND_TICKET);
        assertSuccessfulResponse(response, HttpStatus.OK, List.of());
    }

    @Test
    @DisplayName("Delete ticket flight - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(ticketFlightService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.of(TICKET_FLIGHT));
        doNothing().when(ticketFlightService).delete(TICKET_FLIGHT_ID);

        ResponseEntity<?> response = ticketFlightController.delete(TICKET_NO, FLIGHT_ID);

        verify(ticketFlightService).delete(TICKET_FLIGHT_ID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent ticket flight - should return 404 NOT FOUND")
    void delete_NonExistentTicketFlight_ShouldReturnNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(flightService.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));
        when(ticketFlightService.findById(TICKET_FLIGHT_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = ticketFlightController.delete(TICKET_NO, FLIGHT_ID);

        verify(ticketFlightService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}