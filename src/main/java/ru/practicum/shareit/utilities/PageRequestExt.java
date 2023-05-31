package ru.practicum.shareit.utilities;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageRequestExt {
    public static PageRequest of(final int from, final int size, final Sort sort) {
        int page = from / size;
        return PageRequest.of(page, size, sort);
    }

    public static PageRequest of(final int from, final int size) {
        int page = from / size;
        return PageRequest.of(page, size, Sort.unsorted());
    }
}