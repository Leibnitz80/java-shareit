package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String userHeaderId = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(userHeaderId) Long userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("ItemRequestController: POST create");
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByUser(@RequestHeader(userHeaderId) Long userId) {
        log.info("ItemRequestController: POST getAllByUser");
        return itemRequestService.getAllByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                       @RequestParam(defaultValue = "10") @Positive int size,
                                       @RequestHeader(userHeaderId) Long userId) {
        return itemRequestService.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable Long requestId, @RequestHeader(userHeaderId) Long userId) {
        return itemRequestService.getById(requestId, userId);
    }
}
