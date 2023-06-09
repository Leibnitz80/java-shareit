package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        log.info("Server UserController: POST create");
        return UserMapper.toUserDto(userService.create(user));
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        log.info("Server UserController: GET findById, userId = {}", userId);
        return UserMapper.toUserDto(userService.findById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Server UserController: GET getAll");
        return UserMapper.toUserDtoList(userService.getAll());
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @RequestBody UserDto userDto) {
        log.info("Server UserController: PATCH update, userId = {}", userId);
        return UserMapper.toUserDto(userService.update(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Server UserController: DELETE delete, userId = {}", userId);
        userService.delete(userId);
    }
}