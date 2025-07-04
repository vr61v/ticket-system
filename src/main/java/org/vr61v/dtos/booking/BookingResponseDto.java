package org.vr61v.dtos.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BookingResponseDto {

    @Size(min = 6, max = 6)
    private String ref;

    @NotNull
    private OffsetDateTime date;

    @NotNull @Positive
    private Float amount;

}
