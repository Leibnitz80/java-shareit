package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@Validated
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_HEADER_ID = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto,
                                         @NotNull @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("ItemController: POST create, userId = {}", userId);
        return itemClient.create(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId, @RequestHeader(USER_HEADER_ID) Long userId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("ItemController: POST createComment, userId = {}", userId);
        return itemClient.createComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto itemDto,
                                         @NotNull @PathVariable Long itemId,
                                         @NotNull @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("ItemController: PATCH update, userId = {}", userId);
        return itemClient.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@NotNull @PathVariable Long itemId,
                                           @NotNull @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("ItemController: GET findById, userId = {}, itemId = {}",userId, itemId);
        return itemClient.findById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@NotNull @RequestHeader(USER_HEADER_ID) Long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("ItemController: GET getAllByUserId, userId = {}", userId);
        return itemClient.getAllByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByRequest(@RequestParam String text,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("ItemController: GET findByRequest, text = {}", text);
        return itemClient.findByRequest(text, from, size);
    }
}