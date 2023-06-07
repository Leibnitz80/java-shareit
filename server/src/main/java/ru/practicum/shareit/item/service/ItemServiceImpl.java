package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utilities.PageRequestExt;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private static final Comparator<ItemDto> ITEM_DTO_SORT = Comparator.comparing(o -> o.getLastBooking().getStart(), Comparator.nullsLast(Comparator.reverseOrder()));

    @Transactional
    public ItemDto create(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user with id:" + userId + " not found error"));

        Item item = ItemMapper.toItemModel(itemDto, user);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ObjectNotFoundException("request with id:" + itemDto.getRequestId() + " not found error"));
            item.setRequest(itemRequest);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("item with id:" + itemId + " not found error"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new WrongUserException("wrong user:" + item.getOwner().getId() + " is not an owner of " + itemDto);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto findById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("item with id:" + itemId + " not found error"));
        ItemDto itemDto = ItemMapper.toItemDto(item);

        itemDto.setComments(commentRepository.findAllByItemId(itemId)
                .stream().map(CommentMapper::toCommentDto).collect(toList()));

        if (!item.getOwner().getId().equals(userId)) {
            return itemDto;
        }

        List<Booking> lastBooking = bookingRepository.findTop1BookingByItemIdAndEndIsBeforeAndStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(DESC, "end"));
        List<Booking> nextBooking = bookingRepository.findTop1BookingByItemIdAndEndIsAfterAndStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(Sort.Direction.ASC, "end"));

        if (lastBooking.isEmpty() && !nextBooking.isEmpty()) {
            itemDto.setLastBooking(BookingMapper.toBookingBriefDto(nextBooking.get(0)));
            itemDto.setNextBooking(null);
        } else if (!lastBooking.isEmpty() && !nextBooking.isEmpty()){
            itemDto.setLastBooking(BookingMapper.toBookingBriefDto(lastBooking.get(0)));
            itemDto.setNextBooking(BookingMapper.toBookingBriefDto(nextBooking.get(0)));
        }

        return itemDto;
    }

    @Override
    public List<ItemDto> getAllByUserId(Long userId, int from, int size) {
        Set<Item> items = new HashSet<>(itemRepository.findAllByOwnerIdOrderById(userId, PageRequestExt.of(from, size)).toSet());
        if (items.isEmpty()) {
            return new ArrayList<>();
        }
        BookingBriefDto lastBooking;
        BookingBriefDto nextBooking;

        List<ItemDto> itemDtoList = new ArrayList<>();
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookingMap = bookingRepository.findByItemInAndStatusIs(items, BookingStatus.APPROVED, Sort.by(DESC, "end"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        for (Item item : items) {
            lastBooking = new BookingBriefDto();
            nextBooking = new BookingBriefDto();
            ItemDto itemDto = ItemMapper.toItemDto(item);

            if (comments.get(item) != null) {
                itemDto.setComments(comments.get(item).stream()
                        .map(CommentMapper::toCommentDto).collect(Collectors.toList()));
            }

            if (bookingMap.get(item) != null) {
                List<BookingBriefDto> lnBookings = getLastAndNextBookings(bookingMap.get(item));
                if (lnBookings.get(0) != null) lastBooking = lnBookings.get(0);
                if (lnBookings.get(1) != null) nextBooking = lnBookings.get(1);
            }
            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
            itemDtoList.add(itemDto);
        }

        itemDtoList.sort(ITEM_DTO_SORT);
        for (ItemDto itemDto : itemDtoList) {
            if (itemDto.getLastBooking().getBookerId() == null) {
                itemDto.setLastBooking(null);
            }
            if (itemDto.getNextBooking().getBookerId() == null) {
                itemDto.setNextBooking(null);
            }
        }
        return itemDtoList;
    }

    @Override
    public List<Item> findByRequest(String request, int from, int size) {
        if (request == null || request.isBlank()) {
            return new ArrayList<>();
        }

        final String finalRequest = request.toLowerCase();

          return itemRepository.findAll(PageRequestExt.of(from, size)).stream()
                  .filter(x -> x.getAvailable().equals(true))
                  .filter(x -> x.getName().toLowerCase().contains(finalRequest) || x.getDescription().toLowerCase().contains(finalRequest))
                  .collect(toList());
    }

    @Transactional
    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user with id:" + userId + " not found error"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("item with id:" + itemId + " not found error"));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(userId, itemId, BookingStatus.APPROVED,
                LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException("Wrong item for leaving comment");
        }
        Comment comment = CommentMapper.toCommentModel(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }

    private List<BookingBriefDto> getLastAndNextBookings(List<Booking> bookings) {
        List<BookingBriefDto> resultList = new ArrayList<>();
        Booking lastBooking = null;
        Booking nextBooking = null;
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : bookings) {
            if (lastBooking == null && booking.getEnd().isBefore(now)) {
                lastBooking = booking;
            } else if (booking.getEnd().isBefore(now) && booking.getEnd().isAfter(lastBooking.getEnd())) {
                lastBooking = booking;
            }

            if (nextBooking == null && booking.getStart().isAfter(now)) {
                nextBooking = booking;
            } else if (booking.getStart().isAfter(now) && booking.getStart().isBefore(nextBooking.getStart())) {
                nextBooking = booking;
            }

        }
        if (lastBooking == null && nextBooking != null) {
            lastBooking  = nextBooking;
            nextBooking = null;
        }
        resultList.add(BookingMapper.toBookingBriefDto(lastBooking));
        resultList.add(BookingMapper.toBookingBriefDto(nextBooking));
        return resultList;
    }
}