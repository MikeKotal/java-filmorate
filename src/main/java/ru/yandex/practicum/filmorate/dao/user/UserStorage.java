package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findUsers();

    List<User> findFriends(Long id);

    Optional<User> findUserById(Long id);

    Boolean hasUserByEmail(String email);

    User saveUser(User newUser);

    User updateUser(User newUser);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);
}
