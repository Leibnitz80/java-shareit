package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        log.info("UserController: POST create");
        return UserMapper.toUserDto(userService.create(user));
    }

    @GetMapping("/{userId}")
    public UserDto findById(@NotNull @PathVariable Long userId) {
        log.info("UserController: GET findById, userId = " + userId);
        return UserMapper.toUserDto(userService.findById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return UserMapper.toUserDtoList(userService.getAll());
    }

    @PatchMapping("/{userId}")
    public UserDto update(@NotNull @PathVariable Long userId,
                          @RequestBody UserDto userDto) {
        log.info("UserController: PATCH update, userId = " + userId);
        return UserMapper.toUserDto(userService.update(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public void delete(@NotNull @PathVariable Long userId) {
        log.info("UserController: DELETE delete, userId = " + userId);
        userService.delete(userId);
    }
}