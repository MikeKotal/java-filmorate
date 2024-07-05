package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findUsers() {
        return userStorage.findUsers();
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public User createUser(User newUser) {
        return userStorage.createUser(newUser);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public Collection<User> findUserFriends(Long id) {
        if (id <= 0) {
            throw new ValidationException("В строке запроса было передано отрицательное значение");
        }
        User user = userStorage.findUserById(id);
        Collection<User> userFriends = user.getFriendIds()
                .stream()
                .map(userStorage::findUserById)
                .toList();
        log.info("User friends {}", userFriends);
        return userFriends;
    }

    public Collection<User> findCommonFriends(Long id, Long otherId) {
        checkEmptyFields(id, otherId);
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(otherId);
        if (user.equals(friend)) {
            throw new ConditionsNotMetException("Нельзя посмотреть общих друзей самим с собой");
        }
        Set<Long> userFriends = user.getFriendIds();
        Set<Long> friendFriends = friend.getFriendIds();
        Collection<User> commonFriends = userFriends.stream()
                .filter(friendFriends::contains)
                .map(userStorage::findUserById)
                .toList();
        log.info("Common friends: {}", commonFriends);
        return commonFriends;
    }

    public void addFriend(Long id, Long friendId) {
        checkEmptyFields(id, friendId);
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        if (user.equals(friend)) {
            throw new ConditionsNotMetException("Самого себя нельзя добавить в друзья");
        }
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
        log.info("User: {} added friend: {}", user, friend);
    }

    public void deleteFriend(Long id, Long friendId) {
        checkEmptyFields(id, friendId);
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);
        if (user.equals(friend)) {
            throw new ConditionsNotMetException("Самого себя удалить из друзей невозможно");
        }
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
        log.info("User: {} deleted friend: {}", user, friend);
    }

    private void checkEmptyFields(Long firstId, Long secondId) {
        if (firstId <= 0 || secondId <= 0) {
            throw new ValidationException("В строке запроса было передано отрицательное значение");
        }
    }
}