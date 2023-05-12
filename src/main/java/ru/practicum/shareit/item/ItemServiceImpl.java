package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDao;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemStorage;
    private final UserDao userStorage;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        User user = userStorage.findById(userId);
        Item item = ItemMapper.toItemModel(itemDto,user);

        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(ItemDto itemDto,Long itemId, Long userId) {
        User user = userStorage.findById(userId);
        Item item = ItemMapper.toItemModel(itemDto,user);
        item.setId(itemId);
        return ItemMapper.toItemDto(itemStorage.update(item));
    }

    @Override
    public Item findById(Long itemId) {
        return itemStorage.findById(itemId);
    }

    @Override
    public List<Item> getAllByUserId(Long userId) {
        return itemStorage.getAllByUserId(userId);
    }

    @Override
    public List<Item> findByRequest(String request) {
        if (request == null || request.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.findByRequest(request);
    }
}