package ru.practicum.shareit.booking;

public enum BookingState {
    ALL("Поиск по всем статусам"),
    FUTURE("Поиск запланированных бронирований"),
    CURRENT("Поиск текущих бронирований"),
    PAST("Поиск исполненных бронирований"),
    WAITING("Поиск бронирований в ожидании подтверждении"),
    REJECTED("Поиск отклонённых бронирований");

    private final String title;

    BookingState(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}