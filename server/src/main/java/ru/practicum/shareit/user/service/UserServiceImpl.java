package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(long userId, UserDto userDto) {
        User previousUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user with id:" + userId + " not found error"));

        if (userDto.getName() != null) {
            previousUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(previousUser.getEmail())) {
            previousUser.setEmail(userDto.getEmail());
        }

        return previousUser;
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user with id:" + userId + " not found error"));
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}