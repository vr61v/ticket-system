package org.vr61v.dtos.ticket;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.vr61v.entities.embedded.ContactData;

@Data
@Builder
public class TicketRequestDto {

    @Size(min = 13, max = 13)
    private String ticketNo;

    @NotNull @Valid
    private String bookRef;

    @NotNull @Size(min = 11, max = 11)
    @Pattern(regexp = "^\\d{4} \\d{6}$")
    private String passengerId;

    @NotNull @NotBlank
    private String passengerName;

    @NotNull @Valid
    private ContactData contactData;

}
