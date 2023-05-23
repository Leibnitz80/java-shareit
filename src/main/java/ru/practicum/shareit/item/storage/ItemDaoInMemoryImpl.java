package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class ItemDaoInMemoryImpl implements ItemDao {
    private long idGenerator;
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItems = new HashMap<>();

    @Override
    public Item create(Item item) {
        Long id = getNextId();
        item.setId(id);
        items.put(id, item);
        if (userItems.containsKey(item.getOwner().getId()))
            userItems.get(item.getOwner().getId()).add(item);
        else
            userItems.put(item.getOwner().getId(), new ArrayList<>(List.of(item)));

        log.info("new item added: {}", item.getName());
        return item;
    }

    @Override
    public Item update(Item item) {
        checkItemInStorage(item.getId());

        Item updatedItem = items.get(item.getId());

        if (!updatedItem.getOwner().getId().equals(item.getOwner().getId())) {
            throw new WrongUserException("wrong user:" + item.getOwner() + " is not an owner of " + item);
        }

        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        return updatedItem;
    }

    @Override
    public Item findById(Long itemId) {
        checkItemInStorage(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllByUserId(Long userId) {
        return userItems.get(userId);
    }

    @Override
    public List<Item> findByRequest(String request) {
        List<Item> result = new ArrayList<>();
        request = request.toLowerCase();

        for (Item item : items.values()) {
            String name = item.getName().toLowerCase();
            String description = item.getDescription().toLowerCase();

            if (item.getAvailable() && (name.contains(request) || description.contains(request))) {
                result.add(item);
            }
        }
        return result;
    }

    private Long getNextId() {
        return ++idGenerator;
    }

    private void checkItemInStorage(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ObjectNotFoundException("item with id:" + itemId + " not found error");
        }
    }

    public void clearDataForTesting() {
        idGenerator = 0;
        items.clear();
    }
}