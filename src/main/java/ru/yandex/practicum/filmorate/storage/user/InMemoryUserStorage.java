package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long counter = 0L;

    @Override
    public Collection<User> findUsers() {
        Collection<User> result = users.values();
        log.info("Users {}", result);
        return result;
    }

    @Override
    public User findUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователя с таким id = %s не найдено", id));
        }
        return users.get(id);
    }

    @Override
    public User createUser(User newUser) {
        if (users.containsValue(newUser)) {
            throw new ValidationException("Пользователь с таким email уже создан");
        }
        newUser.setId(++counter);
        newUser.setName((newUser.getName() == null || newUser.getName().isBlank())
                ? newUser.getLogin()
                : newUser.getName());
        log.info("New created user {}", newUser);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Поле id не должно быть пустым");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException(String.format("Пользователя с таким id = %s не найдено", newUser.getId()));
        }
        if (users.containsValue(newUser)) {
            throw new ValidationException("Пользователь с таким email уже есть");
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
