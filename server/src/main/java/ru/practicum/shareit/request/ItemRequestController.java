package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_HEADER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(USER_HEADER_ID) Long userId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Server ItemRequestController: POST create, userId = {}", userId);
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByUser(@RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server ItemRequestController: GET getAllByUser, userId = {}", userId);
        return itemRequestService.getAllByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server ItemRequestController: GET getAll, userId = {}", userId);
        return itemRequestService.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable Long requestId, @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server ItemRequestController: GET getById, userId = {}, requestId = {}", userId, requestId);
        return itemRequestService.getById(requestId, userId);
    }
}