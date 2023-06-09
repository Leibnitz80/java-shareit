package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_HEADER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto,
                          @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server ItemController: POST create, userId = {}", userId);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable Long itemId,
                          @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server ItemController: PATCH update, userId = {}", userId);
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId,
                            @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Server ItemController: GET findById, userId = {}, itemId = {}",userId, itemId);
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader(USER_HEADER_ID) Long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Server ItemController: GET getAllByUserId, userId = {}", userId);
        return itemService.getAllByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> findByRequest(@RequestParam String text,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        List<Item> foundItems = itemService.findByRequest(text, from, size);
        log.info("Server ItemController: GET findByRequest, text = {}", text);
        return ItemMapper.toItemDtoList(foundItems);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId,
                                    @RequestHeader(USER_HEADER_ID) Long userId,
                                    @RequestBody CommentDto commentDto) {
        log.info("Server ItemController: POST createComment, userId = {}", userId);
        return itemService.createComment(itemId, userId, commentDto);
    }
}