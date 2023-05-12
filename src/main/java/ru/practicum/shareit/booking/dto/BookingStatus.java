package ru.practicum.shareit.booking.dto;

public enum BookingStatus {
    WAITING("В ожидании подтверждения"),
    APPROVED("Подтверждён"),
    REJECTED("Отклонён владельцем"),
    CANCELED("Удалён заявителем");

    private String description;

    BookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}