package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_HEADER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto create(@RequestBody BookingBriefDto bookingBriefDto,
                             @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server BookingController: POST create, userId = {}" , userId);
        return bookingService.create(bookingBriefDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable Long bookingId, @RequestHeader(USER_HEADER_ID) Long userId,
                              @RequestParam Boolean approved) {
        log.info("Server BookingController: PATCH approve, userId = {}; bookingId = {}" ,userId, bookingId);
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(USER_HEADER_ID) Long userId,
                                          @RequestParam(defaultValue = "ALL") String state,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Server BookingController: GET getAllByOwner, userId = {}; state = {}",userId, state);
        return bookingService.getAllByOwner(userId, state, from, size);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader(USER_HEADER_ID) Long userId,
                                         @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Server BookingController: GET getAllByUser, userId = {}; state = {}",userId, state);
        return bookingService.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId, @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server BookingController: GET getById, userId = {}; bookingId = {}",userId, bookingId);
        return bookingService.getById(bookingId, userId);
    }
}