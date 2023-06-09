package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utilities.PageRequestExt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository requestRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private final List<Item> items = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<ItemRequest> requests = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("ivanov@mail.ru");
        users.add(userRepository.save(user1));

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("petrov@yandex.ru");
        users.add(userRepository.save(user2));

        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Description 1");
        request1.setRequestor(user1);
        request1.setCreated(LocalDateTime.now());
        requests.add(requestRepository.save(request1));

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Description 2");
        request2.setRequestor(user2);
        request2.setCreated(LocalDateTime.now());
        requests.add(requestRepository.save(request2));

        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Description Item 1 ");
        item1.setOwner(user2);
        item1.setAvailable(true);
        item1.setRequest(request1);
        items.add(itemRepository.save(item1));

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Description Item 2");
        item2.setOwner(user1);
        item2.setAvailable(true);
        item2.setRequest(request2);
        items.add(itemRepository.save(item2));

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setBooker(user1);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(4));
        booking1.setStatus(BookingStatus.APPROVED);
        bookings.add(bookingRepository.save(booking1));

        Booking booking2 = new Booking();
        booking1.setId(2L);
        booking2.setBooker(user2);
        booking2.setItem(item2);
        booking2.setStart(LocalDateTime.now().plusDays(3));
        booking2.setEnd(LocalDateTime.now().plusDays(5));
        booking2.setStatus(BookingStatus.APPROVED);
        bookings.add(bookingRepository.save(booking2));
    }

    @AfterEach
    public void afterEach() {
        users.clear();
        items.clear();
        requests.clear();
        bookings.clear();
        requestRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findAllByItemOwnerIdAndStartBeforeAndEndAfterTest() {
        Pageable pageable = PageRequestExt.of(0, 1);

        Assertions.assertEquals(List.of(bookings.get(0)),
                bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(users.get(1),
                        LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3), pageable).toList());

        Assertions.assertEquals(List.of(bookings.get(1)),
                bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(users.get(0),
                        LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4), pageable).toList());
    }

    @Test
    public void findAllByBookerAndStartBeforeAndEndAfterTest() {
        Pageable pageable = PageRequestExt.of(0, 1);

        Assertions.assertEquals(List.of(bookings.get(0)),
                bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(users.get(0),
                        LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3), pageable).toList());

        Assertions.assertEquals(List.of(bookings.get(1)),
                bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(users.get(1),
                        LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4), pageable).toList());
    }
}