package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findUserFriends(Long id) {
        User user = userStorage.findUserById(id);
        if (user.getFriendIds() == null) {
            log.info("User has no friends {}", user);
            return Collections.emptyList();
        }
        Collection<User> userFriends = userStorage.findUsers()
                .stream()
                .filter(friends -> user.getFriendIds()
                        .stream()
                        .anyMatch(friend -> friend.equals(friends.getId())))
                .toList();
        log.info("User friends {}", userFriends);
        return userFriends;
    }

    public Collection<User> findCommonFriends(Long id, Long otherId) {
        User user = getFriendUsers(id, otherId).getFirst();
        User friend = getFriendUsers(id, otherId).getLast();
        if (user.getFriendIds() == null || friend.getFriendIds() == null) {
            log.info("One of users have no friends {}, {}", user, friend);
            return Collections.emptyList();
        }
        List<Long> commonFriendIds = user.getFriendIds()
                .stream()
                .filter(userFriend -> friend.getFriendIds()
                        .stream()
                        .anyMatch(otherFriend -> otherFriend.equals(userFriend)))
                .toList();
        Collection<User> commonFriends = userStorage.findUsers()
                .stream()
                .filter(currentUser -> commonFriendIds
                        .stream()
                        .anyMatch(commonFriend -> commonFriend.equals(currentUser.getId())))
                .toList();
        log.info("Common friends: {}", commonFriends);
        return commonFriends;
    }

    public void addFriend(Long id, Long friendId) {
        User user = getFriendUsers(id, friendId).getFirst();
        User friend = getFriendUsers(id, friendId).getLast();
        Set<Long> userFriends = user.getFriendIds() == null ? new HashSet<>() : user.getFriendIds();
        Set<Long> friendFriends = friend.getFriendIds() == null ? new HashSet<>() : friend.getFriendIds();
        userFriends.add(friend.getId());
        friendFriends.add(user.getId());
        user.setFriendIds(userFriends);
        friend.setFriendIds(friendFriends);
        log.info("User: {} added friend: {}", user, friend);
    }

    public void deleteFriend(Long id, Long friendId) {
        User user = getFriendUsers(id, friendId).getFirst();
        User friend = getFriendUsers(id, friendId).getLast();
        Set<Long> userFriends = user.getFriendIds() == null ? new HashSet<>() : user.getFriendIds();
        Set<Long> friendFriends = friend.getFriendIds() == null ? new HashSet<>() : friend.getFriendIds();
        userFriends.remove(friend.getId());
        friendFriends.remove(user.getId());
        user.setFriendIds(userFriends);
        friend.setFriendIds(friendFriends);
        log.info("User: {} deleted friend: {}", user, friend);
    }

    private List<User> getFriendUsers(Long id, Long friendId) {
        User user = userStorage.findUsers()
                .stream()
                .filter(currentUser -> currentUser.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователя с таким id = %s не найдено", id)));
        User friend = userStorage.findUsers()
                .stream()
                .filter(friendUser -> friendUser.getId().equals(friendId))
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException(String.format("Друга с таким friendId = %s не найдено", friendId))
                );
        if (user.equals(friend)) {
            throw new ConditionsNotMetException("Недопустимое действие с самим собой");
        }
        return List.of(user, friend);
    }
}