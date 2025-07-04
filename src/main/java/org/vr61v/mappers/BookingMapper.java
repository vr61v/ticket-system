package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.booking.BookingRequestDto;
import org.vr61v.dtos.booking.BookingResponseDto;
import org.vr61v.entities.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "ref", source = "entity.bookRef")
    @Mapping(target = "date", source = "entity.bookDate")
    @Mapping(target = "amount", source = "entity.totalAmount")
    BookingResponseDto toDto(Booking entity);

    @Mapping(target = "bookDate", source = "dto.date")
    @Mapping(target = "totalAmount", source = "dto.amount")
    Booking toEntity(BookingRequestDto dto);

}
