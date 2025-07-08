package org.vr61v.dtos.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class BookingRequestDto {

    @NotNull
    private OffsetDateTime date;

    @NotNull @Positive
    private Float amount;

}
