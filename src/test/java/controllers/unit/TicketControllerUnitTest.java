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
import org.vr61v.controllers.v1.TicketController;
import org.vr61v.dtos.ticket.TicketRequestDto;
import org.vr61v.dtos.ticket.TicketResponseDto;
import org.vr61v.entities.Booking;
import org.vr61v.entities.Ticket;
import org.vr61v.entities.embedded.ContactData;
import org.vr61v.mappers.TicketMapper;
import org.vr61v.services.impl.BookingService;
import org.vr61v.services.impl.TicketService;

import java.util.List;
import java.util.Optional;

import static controllers.unit.util.CommonAssertions.assertErrorResponse;
import static controllers.unit.util.CommonAssertions.assertSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerUnitTest {

    private static final String TICKET_NO = "0000000000000";
    private static final String BOOK_REF = "000000";
    private static final String PASSENGER_ID = "0000 000000";
    private static final String PASSENGER_NAME = "PASSENGER NAME";
    private static final ContactData CONTACT_DATA = new ContactData("+70000000000", "email@test.com");

    private static final Booking BOOKING = Booking.builder()
            .bookRef(BOOK_REF)
            .build();

    private static final Ticket TICKET = Ticket.builder()
            .ticketNo(TICKET_NO)
            .booking(BOOKING)
            .passengerId(PASSENGER_ID)
            .passengerName(PASSENGER_NAME)
            .contactData(CONTACT_DATA)
            .build();

    private static final List<Ticket> TICKET_LIST = List.of(TICKET);

    private static final TicketRequestDto REQUEST = TicketRequestDto.builder()
            .bookRef(BOOK_REF)
            .passengerId(PASSENGER_ID)
            .passengerName(PASSENGER_NAME)
            .contactData(CONTACT_DATA)
            .build();

    private static final TicketResponseDto RESPONSE = TicketResponseDto.builder()
            .ticketNo(TICKET_NO)
            .booking(null) // будет замокан в тестах
            .passengerId(PASSENGER_ID)
            .passengerName(PASSENGER_NAME)
            .contactData(CONTACT_DATA)
            .build();

    private static final List<TicketResponseDto> RESPONSE_LIST = List.of(RESPONSE);

    @Mock
    private TicketService ticketService;

    @Mock
    private BookingService bookingService;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketController ticketController;

    @Test
    @DisplayName("Create ticket - should return 201 CREATED when booking exists")
    void create_ShouldReturnCreated_WhenBookingExists() {
        when(bookingService.findById(BOOK_REF)).thenReturn(Optional.of(BOOKING));
        when(ticketService.create(any(Ticket.class))).thenReturn(TICKET);
        when(ticketMapper.toEntity(any(TicketRequestDto.class))).thenReturn(TICKET);
        when(ticketMapper.toDto(TICKET)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketController.create(TICKET_NO, REQUEST);

        verify(bookingService).findById(BOOK_REF);
        verify(ticketService).create(argThat(ticket ->
                ticket.getTicketNo().equals(TICKET_NO) &&
                        ticket.getBooking().equals(BOOKING) &&
                        ticket.getPassengerId().equals(PASSENGER_ID) &&
                        ticket.getPassengerName().equals(PASSENGER_NAME) &&
                        ticket.getContactData().equals(CONTACT_DATA)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Create ticket - should throw 404 when booking not found")
    void create_ShouldThrowNotFound_WhenBookingNotFound() {
        when(bookingService.findById(BOOK_REF)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketController.create(TICKET_NO, REQUEST))
                .isInstanceOf(HttpClientErrorException.class)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.NOT_FOUND);

        verify(ticketService, never()).create(any());
    }

    @Test
    @DisplayName("Update ticket - should return 200 OK when booking exists")
    void update_ShouldReturnOk_WhenBookingExists() {
        when(bookingService.findById(BOOK_REF)).thenReturn(Optional.of(BOOKING));
        when(ticketService.update(any(Ticket.class))).thenReturn(TICKET);
        when(ticketMapper.toEntity(any(TicketRequestDto.class))).thenReturn(TICKET);
        when(ticketMapper.toDto(TICKET)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketController.update(TICKET_NO, REQUEST);

        verify(ticketService).update(argThat(ticket ->
                ticket.getTicketNo().equals(TICKET_NO) &&
                        ticket.getBooking().equals(BOOKING) &&
                        ticket.getPassengerId().equals(PASSENGER_ID) &&
                        ticket.getPassengerName().equals(PASSENGER_NAME) &&
                        ticket.getContactData().equals(CONTACT_DATA)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find ticket by ID - should return 200 OK with ticket data")
    void findById_ShouldReturnTicket() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        when(ticketMapper.toDto(TICKET)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketController.findById(TICKET_NO);

        verify(ticketService).findById(TICKET_NO);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find non-existent ticket - should return 404 NOT FOUND")
    void findById_NonExistentTicket_ShouldReturnNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.empty());

        ResponseEntity<?> response = ticketController.findById(TICKET_NO);

        verify(ticketService).findById(TICKET_NO);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Find all tickets by booking reference - should return list of tickets")
    void findAllByBookRef_ShouldReturnTicketList() {
        when(ticketService.findByBookRef(BOOK_REF)).thenReturn(TICKET_LIST);
        when(ticketMapper.toDto(TICKET)).thenReturn(RESPONSE);

        ResponseEntity<?> response = ticketController.findAllByBookRef(BOOK_REF);

        verify(ticketService).findByBookRef(BOOK_REF);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LIST);
    }

    @Test
    @DisplayName("Find all tickets for non-existent booking - should return empty list")
    void findAllByBookRef_NonExistentBooking_ShouldReturnEmptyList() {
        when(ticketService.findByBookRef("UNKNOWN")).thenReturn(List.of());

        ResponseEntity<?> response = ticketController.findAllByBookRef("UNKNOWN");

        verify(ticketService).findByBookRef("UNKNOWN");
        assertSuccessfulResponse(response, HttpStatus.OK, List.of());
    }

    @Test
    @DisplayName("Delete ticket - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.of(TICKET));
        doNothing().when(ticketService).delete(TICKET_NO);

        ResponseEntity<?> response = ticketController.delete(TICKET_NO);

        verify(ticketService).delete(TICKET_NO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent ticket - should return 404 NOT FOUND")
    void delete_NonExistentTicket_ShouldReturnNotFound() {
        when(ticketService.findById(TICKET_NO)).thenReturn(Optional.empty());

        ResponseEntity<?> response = ticketController.delete(TICKET_NO);

        verify(ticketService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}