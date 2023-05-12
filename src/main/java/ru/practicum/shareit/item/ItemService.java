package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto item,Long userId);

    ItemDto update(ItemDto item, Long itemId, Long userId);

    Item findById(Long itemId);

    List<Item> getAllByUserId(Long userId);

    List<Item> findByRequest(String request);
}