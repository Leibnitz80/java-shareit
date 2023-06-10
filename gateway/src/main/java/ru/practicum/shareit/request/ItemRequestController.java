package ru.practicum.shareit.request;

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
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String USER_HEADER_ID = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER_ID) Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("ItemRequestController: POST create, userId = {}", userId);
        return requestClient.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("ItemRequestController: GET getAllByUser, userId = {}", userId);
        return requestClient.getAllByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size,
                                         @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("ItemRequestController: GET getAll, userId = {}", userId);
        return requestClient.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId, @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("ItemRequestController: GET getById, userId = {}, requestId = {}", userId, requestId);
        return requestClient.getById(requestId, userId);
    }
}