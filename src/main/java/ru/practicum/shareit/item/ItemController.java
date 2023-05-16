package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String userHeaderId = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @NotNull @RequestHeader(userHeaderId) Long userId) {
        log.info("ItemController: POST create, userId = " + userId);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @NotNull @PathVariable Long itemId,
                          @NotNull @RequestHeader(userHeaderId) Long userId) {
        log.info("ItemController: PATCH update, userId = " + userId);
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@NotNull @PathVariable Long itemId,
                            @NotNull @RequestHeader(userHeaderId) Long userId) {
        log.info("ItemController: GET findById, userId = " + userId + ", itemId = " + itemId);
        return itemService.findById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(@NotNull @RequestHeader(userHeaderId) Long userId) {
        log.info("ItemController: GET getAllByUserId, userId = " + userId);
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByRequest(@RequestParam String text) {
        List<Item> foundItems = itemService.findByRequest(text);
        log.info("ItemController: GET findByRequest, text = " + text);
        return ItemMapper.toItemDtoList(foundItems);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId, @RequestHeader(userHeaderId) Long userId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("ItemController: POST createComment, userId = " + userId);
        return itemService.createComment(itemId, userId, commentDto);
    }
}