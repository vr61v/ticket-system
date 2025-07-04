package org.vr61v.dtos.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BookingRequestDto {

    @NotNull
    private OffsetDateTime date;

    @NotNull @Positive
    private Float amount;

}
