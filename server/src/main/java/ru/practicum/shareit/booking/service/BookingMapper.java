package ru.practicum.shareit.booking.service;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto result = new BookingDto();
        result.setId(booking.getId());
        result.setStart(booking.getStart());
        result.setEnd(booking.getEnd());
        result.setStatus(booking.getStatus());
        result.setBooker(booking.getBooker());
        result.setItem(booking.getItem());
        return result;
    }

    public static BookingBriefDto toBookingBriefDto(Booking booking) {
        BookingBriefDto result = new BookingBriefDto();
        result.setId(booking.getId());
        result.setStart(booking.getStart());
        result.setEnd(booking.getEnd());
        result.setItemId(booking.getItem().getId());
        result.setBookerId(booking.getBooker().getId());
        return result;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking result = new Booking();
        result.setId(bookingDto.getId());
        result.setStart(bookingDto.getStart());
        result.setEnd(bookingDto.getEnd());
        result.setBooker(bookingDto.getBooker());
        result.setStatus(bookingDto.getStatus());
        result.setItem(bookingDto.getItem());
        return result;
    }

    public static Booking toBooking(BookingBriefDto bookingBriefDto) {
        Booking result = new Booking();
        result.setStart(bookingBriefDto.getStart());
        result.setEnd(bookingBriefDto.getEnd());

        return result;
    }
}
