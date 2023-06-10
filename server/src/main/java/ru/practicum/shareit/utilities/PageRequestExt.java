package ru.practicum.shareit.utilities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


public class PageRequestExt extends PageRequest{
    protected PageRequestExt(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public static PageRequestExt of(final int from, final int size, final Sort sort) {
        int page = from / size;
        return new PageRequestExt(page, size, sort);
    }

    public static PageRequestExt of(final int from, final int size) {
        int page = from / size;
        return new PageRequestExt(page, size, Sort.unsorted());
    }
}