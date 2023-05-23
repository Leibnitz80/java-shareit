package ru.practicum.shareit.user.service;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUserModel(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(x -> UserMapper.toUserDto(x)).collect(Collectors.toList());
    }
}