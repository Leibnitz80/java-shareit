package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String USER_HEADER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingBriefDto bookingBriefDto,
                                         @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("BookingController: POST create, userId = {}" , userId);
        return bookingClient.create(bookingBriefDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable Long bookingId,
                                          @RequestHeader(USER_HEADER_ID) Long userId,
                                          @RequestParam Boolean approved) {
        log.info("BookingController: PATCH approve, userId = {}; bookingId = {}" ,userId, bookingId);
        return bookingClient.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@PathVariable Long bookingId, @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("BookingController: GET getById, userId = {}; bookingId = {}",userId, bookingId);
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(USER_HEADER_ID) Long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("BookingController: GET getAllByUser, userId = {}; state = {}",userId, state);
        return bookingClient.getAllByUser(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(USER_HEADER_ID) Long userId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("BookingController: GET getAllByOwner, userId = {}; state = {}",userId, state);
        return bookingClient.getAllByOwner(state, userId, from, size);
    }
}