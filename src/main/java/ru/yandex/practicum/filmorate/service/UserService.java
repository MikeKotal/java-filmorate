package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<UserDto> getUsers() {
        List<UserDto> users = userStorage.findUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        log.info("Все пользователи {}", users);
        return users;
    }

    public UserDto getUserById(Long id) {
        log.info("Запрос на получение пользователя за id {}", id);
        UserDto userDto = UserMapper.mapToUserDto(getUser(id));
        log.info("Пользователь с id '{} = '{}'", id, userDto);
        return userDto;
    }

    public UserDto createUser(UserRequest request) {
        log.info("Запрос на создание пользователя {}", request);
        User newUser = UserMapper.mapToUser(request);
        newUser.setName((newUser.getName() == null || newUser.getName().isBlank())
                ? newUser.getLogin()
                : newUser.getName());
        newUser = userStorage.saveUser(newUser);
        log.info("пользователь успешно создан {}", newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    public UserDto updateUser(UserRequest request) {
        log.info("Запрос на обновление пользователя {}", request);
        User newUser = UserMapper.mapToUser(request);
        if (newUser.getId() == null) {
            log.error("Отсутствует идентификатор пользователя при обновлении");
            throw new ValidationException("Не был передан идентификатор пользователя");
        }
        if (newUser.getId() <= 0) {
            log.error("Неккорректный идентификатор пользователя {}", newUser.getId());
            throw new ValidationException("Идентификатор пользователя не должен быть 0 или отрицательным");
        }
        User oldUser = getUser(newUser.getId());
        newUser.setId(oldUser.getId());
        newUser.setName((newUser.getName() == null || newUser.getName().isBlank())
                ? newUser.getLogin()
                : newUser.getName());
        newUser = userStorage.updateUser(newUser);
        log.info("Пользователь успешно обновлен {}", newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    public List<UserDto> getUserFriends(Long id) {
        User user = getUser(id);
        List<UserDto> userFriends = userStorage.findFriends(id)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        log.info("Друзья пользователя {}, {}", user, userFriends);
        return userFriends;
    }

    public List<UserDto> getCommonFriends(Long id, Long otherId) {
        log.info("Запрос на получение общих друзей id = {}, friendId = {}", id, otherId);
        User user = getUser(id);
        User friend = getUser(otherId);
        if (user.equals(friend)) {
            log.error("Пользователи идентичны друг другу 1 = {}, 2 = {}", user, friend);
            throw new ConditionsNotMetException("Нельзя посмотреть общих друзей самим с собой");
        }
        List<UserDto> commonFriends = userStorage.findCommonFriends(id, otherId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        log.info("Список общих друзей: {}", commonFriends);
        return commonFriends;
    }

    public void addFriend(Long id, Long friendId) {
        log.info("Запрос на добавление в друзья, user = {}, friend = {}", id, friendId);
        User user = getUser(id);
        User friend = getUser(friendId);
        if (user.equals(friend)) {
            log.error("Самого себя добавить нельзя user = {}, friend = {}", user, friend);
            throw new ConditionsNotMetException("Самого себя нельзя добавить в друзья");
        }
        userStorage.addFriend(id, friendId);
        log.info("Пользователь: {} добавил друга: {}", user, friend);
    }

    public void deleteFriend(Long id, Long friendId) {
        log.info("Запрос на удаление из друзей, user = {}, friend = {}", id, friendId);
        User user = getUser(id);
        User friend = getUser(friendId);
        if (user.equals(friend)) {
            log.error("Попытка удалить самого себя из друзей, user = {}, friend = {}", user, friend);
            throw new ConditionsNotMetException("Самого себя удалить из друзей невозможно");
        }
        userStorage.deleteFriend(id, friendId);
        log.info("Пользователь: {} удалил друга: {}", user, friend);
    }

    private User getUser(Long id) {
        return userStorage.findUserById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} отсутствует", id);
                    return new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", id));
                });
    }
}