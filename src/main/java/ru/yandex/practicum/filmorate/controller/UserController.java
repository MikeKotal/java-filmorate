package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long counter = 0L;

    @GetMapping
    public Collection<User> getUsers() {
        Collection<User> result = users.values();
        log.info("Users {}", result);
        return result;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        log.info("Create user {}", newUser);
        newUser.setId(++counter);
        newUser.setName((newUser.getName() == null || newUser.getName().isBlank())
                ? newUser.getLogin()
                : newUser.getName());
        log.info("New created user {}", newUser);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("Update user {}", newUser);
        if (newUser.getId() == null) {
            ValidationException validationException = new ValidationException("Поле id не должно быть пустым");
            log.error(validationException.getMessage(), validationException);
            throw validationException;
        }
        if (!users.containsKey(newUser.getId())) {
            NotFoundException notFoundException
                    = new NotFoundException(String.format("Пользователя с таким id = %s не найдено", newUser.getId()));
            log.error(notFoundException.getMessage(), notFoundException);
            throw notFoundException;
        }
        User oldUser = users.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName((newUser.getName() == null || newUser.getName().isBlank())
                ? newUser.getLogin()
                : newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.info("Updated user {}", oldUser);
        return oldUser;
    }
}
