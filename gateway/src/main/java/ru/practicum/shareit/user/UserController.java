package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@Slf4j
@Validated
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("UserController: POST create");
        return userClient.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@NotNull @PathVariable Long userId) {
        log.info("UserController: GET findById, userId = {}", userId);
        return userClient.findById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("UserController: GET getAll");
        return userClient.getAll();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@NotNull @PathVariable Long userId,
                                         @RequestBody UserDto userDto) {
        log.info("UserController: PATCH update, userId = {}", userId);
        return userClient.update(userId, userDto);
    }

    @ResponseBody
    @DeleteMapping("/{userId}")
    public void delete(@NotNull @PathVariable Long userId) {
        log.info("UserController: DELETE delete, userId = {}", userId);
        userClient.delete(userId);
    }
}