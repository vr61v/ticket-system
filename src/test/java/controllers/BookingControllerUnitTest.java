package controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vr61v.controllers.v1.BookingController;
import org.vr61v.dtos.booking.BookingRequestDto;
import org.vr61v.dtos.booking.BookingResponseDto;
import org.vr61v.entities.Booking;
import org.vr61v.mappers.BookingMapper;
import org.vr61v.services.impl.BookingService;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static controllers.CommonAssertions.assertErrorResponse;
import static controllers.CommonAssertions.assertSuccessfulResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerUnitTest {

    private static final String BOOK_REF = "000000";
    private static final String NOT_FOUND_REF = "XXXXXX";
    private static final OffsetDateTime BOOK_DATE = OffsetDateTime.now();
    private static final Float TOTAL_AMOUNT = 15000.0f;
    private static final LocalDate TEST_DAY = LocalDate.now();

    private static final Booking BOOKING = Booking.builder()
            .bookRef(BOOK_REF)
            .bookDate(BOOK_DATE)
            .totalAmount(TOTAL_AMOUNT)
            .build();

    private static final List<Booking> BOOKING_LIST = List.of(BOOKING);

    private static final BookingRequestDto REQUEST = BookingRequestDto.builder()
            .date(BOOK_DATE)
            .amount(TOTAL_AMOUNT)
            .build();

    private static final BookingResponseDto RESPONSE = BookingResponseDto.builder()
            .ref(BOOK_REF)
            .date(BOOK_DATE)
            .amount(TOTAL_AMOUNT)
            .build();

    private static final List<BookingResponseDto> RESPONSE_LIST = List.of(RESPONSE);

    @Mock
    private BookingService bookingService;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingController bookingController;

    @Test
    @DisplayName("Create booking - should return 201 CREATED with response body")
    void create_ShouldReturnCreated() {
        when(bookingService.create(any(Booking.class))).thenReturn(BOOKING);
        when(bookingMapper.toDto(BOOKING)).thenReturn(RESPONSE);
        when(bookingMapper.toEntity(any(BookingRequestDto.class))).thenReturn(BOOKING);

        ResponseEntity<?> response = bookingController.create(BOOK_REF, REQUEST);

        verify(bookingService).create(argThat(booking ->
                booking.getBookRef().equals(BOOK_REF) &&
                        booking.getBookDate().equals(BOOK_DATE) &&
                        booking.getTotalAmount().equals(TOTAL_AMOUNT)
        ));

        assertSuccessfulResponse(response, HttpStatus.CREATED, RESPONSE);
    }

    @Test
    @DisplayName("Update booking - should return 200 OK with updated booking")
    void update_ShouldReturnOk() {
        when(bookingService.update(any(Booking.class))).thenReturn(BOOKING);
        when(bookingMapper.toDto(BOOKING)).thenReturn(RESPONSE);
        when(bookingMapper.toEntity(any(BookingRequestDto.class))).thenReturn(BOOKING);

        ResponseEntity<?> response = bookingController.update(BOOK_REF, REQUEST);

        verify(bookingService).update(argThat(booking ->
                booking.getBookRef().equals(BOOK_REF) &&
                        booking.getBookDate().equals(BOOK_DATE) &&
                        booking.getTotalAmount().equals(TOTAL_AMOUNT)
        ));

        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find booking by ID - should return 200 OK with booking data")
    void findById_ShouldReturnBooking() {
        when(bookingService.findById(BOOK_REF)).thenReturn(Optional.of(BOOKING));
        when(bookingMapper.toDto(BOOKING)).thenReturn(RESPONSE);

        ResponseEntity<?> response = bookingController.findById(BOOK_REF);

        verify(bookingService).findById(BOOK_REF);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE);
    }

    @Test
    @DisplayName("Find non-existent booking - should return 404 NOT FOUND")
    void findById_NonExistentBooking_ShouldReturnNotFound() {
        when(bookingService.findById(NOT_FOUND_REF)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookingController.findById(NOT_FOUND_REF);

        verify(bookingService).findById(NOT_FOUND_REF);
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Find all bookings by day - should return list of bookings")
    void findAllByDay_ShouldReturnBookingList() {
        OffsetDateTime dayStart = TEST_DAY.atStartOfDay().atOffset(ZoneOffset.UTC);
        when(bookingService.findAllByDay(dayStart)).thenReturn(BOOKING_LIST);
        when(bookingMapper.toDto(BOOKING)).thenReturn(RESPONSE);

        ResponseEntity<?> response = bookingController.findAllByDay(TEST_DAY);

        verify(bookingService).findAllByDay(dayStart);
        assertSuccessfulResponse(response, HttpStatus.OK, RESPONSE_LIST);
    }

    @Test
    @DisplayName("Delete booking - should return 204 NO CONTENT")
    void delete_ShouldReturnNoContent() {
        when(bookingService.findById(BOOK_REF)).thenReturn(Optional.of(BOOKING));
        doNothing().when(bookingService).delete(BOOK_REF);

        ResponseEntity<?> response = bookingController.delete(BOOK_REF);

        verify(bookingService).delete(BOOK_REF);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete non-existent booking - should return 404 NOT FOUND")
    void delete_NonExistentBooking_ShouldReturnNotFound() {
        when(bookingService.findById(NOT_FOUND_REF)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookingController.delete(NOT_FOUND_REF);

        verify(bookingService, never()).delete(any());
        assertErrorResponse(response, HttpStatus.NOT_FOUND);
    }

}
