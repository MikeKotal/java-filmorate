package ru.yandex.practicum.filmorate.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DbStorage;
import ru.yandex.practicum.filmorate.dao.user.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbStorage extends DbStorage<User> implements UserStorage {

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<User> findUsers() {
        String query = "SELECT * FROM users";
        return findMany(query);
    }

    @Override
    public List<User> findFriends(Long id) {
        String query = """
                SELECT users.user_id, users.email, users.login, users.name, users.birthday
                FROM users
                LEFT JOIN friends ON users.user_id=friends.friend_id
                WHERE users.user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)
                AND friends.user_id = ?
                """;
        return findMany(query, id, id);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        return findOne(query, id);
    }

    @Override
    public User saveUser(User newUser) {
        String query = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
        Long id = insert(
                query,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()
        );
        newUser.setId(id);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        String query = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        update(
                query,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );
        return newUser;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String query = "INSERT INTO friends(user_id, friend_id) VALUES (?, ?)";
        insertWithoutKey(query, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        delete(query, userId, friendId);
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        String query = """
                SELECT *
                FROM USERS AS u, FRIENDS AS friends, FRIENDS AS others
                WHERE u.USER_ID = friends.FRIEND_ID
                AND u.USER_ID = others.FRIEND_ID
                AND friends.USER_ID = ?
                AND others.USER_ID = ?
                """;
        return findMany(query, id, otherId);
    }
}
