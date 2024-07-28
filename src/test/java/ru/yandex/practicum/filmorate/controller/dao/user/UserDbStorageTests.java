package ru.yandex.practicum.filmorate.controller.dao.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao.user")
public class UserDbStorageTests {

    private final UserDbStorage userStorage;

    @Test
    public void whenFindAllUsersThenReturnArrayWithLength2() {
        List<User> users = userStorage.findUsers();

        Assertions.assertEquals(2, users.size(), "Некорректное количество пользователей в списке");
    }

    @Test
    public void whenFindFriendsByUserId1ThenReturn1Friend() {
        List<User> friends = userStorage.findFriends(1L);

        Assertions.assertEquals(1, friends.size(), "Некорректное количество друзей в списке");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(2L, friends.getFirst().getId(),
                    "Некорректный идентификатор пользователя");
            Assertions.assertEquals("email@email.com", friends.getFirst().getEmail(),
                    "Некорректный email");
            Assertions.assertEquals("qwerty123", friends.getFirst().getLogin(), "Некорректный логин");
            Assertions.assertEquals("Nick", friends.getFirst().getName(), "Некорректное имя");
            Assertions.assertEquals("1995-07-02", friends.getFirst().getBirthday().toString());
        });
    }

    @Test
    public void whenFindUserById1ThenReturnUser1() {
        User user = userStorage.findUserById(1L).orElse(null);

        Assertions.assertNotNull(user, "Пользователь не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, user.getId(), "Некорректный идентификатор пользователя");
            Assertions.assertEquals("test@test.ru", user.getEmail(), "Некорректный email");
            Assertions.assertEquals("test123", user.getLogin(), "Некорректный логин");
            Assertions.assertEquals("John", user.getName(), "Некорректное имя");
            Assertions.assertEquals("1995-07-01", user.getBirthday().toString(),
                    "Некорректная дата рождения");
        });
    }

    @Test
    public void whenAddNewUserThenUserSuccessCreated() {
        User user = new User();
        String email = "newEmail@email.com";
        String login = "newLogin12345";
        String name = "John Doe";
        LocalDate birthday = LocalDate.parse("1995-07-03");
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);

        userStorage.saveUser(user);

        User savedUser = userStorage.findUserById(4L).orElse(null);

        Assertions.assertNotNull(savedUser, "Новый пользователь не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(4L, savedUser.getId(), "Некорректный идентификатор пользователя");
            Assertions.assertEquals(email, savedUser.getEmail(), "Некорректный email");
            Assertions.assertEquals(login, savedUser.getLogin(), "Некорректный логин");
            Assertions.assertEquals(name, savedUser.getName(), "Некорректное имя");
            Assertions.assertEquals(birthday, savedUser.getBirthday(), "Некорректная дата рождения");
        });
    }

    @Test
    public void whenUpdateUser1ThenReturnUpdatedUser() {
        User user = new User();
        Long id = 1L;
        String email = "newEmail@email.com";
        String login = "newLogin12345";
        String name = "John Doe";
        LocalDate birthday = LocalDate.parse("1995-07-03");
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);

        userStorage.updateUser(user);

        User updatedUser = userStorage.findUserById(1L).orElse(null);

        Assertions.assertNotNull(updatedUser, "Обновленный пользователь не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(id, updatedUser.getId(), "Некорректный идентификатор пользователя");
            Assertions.assertEquals(email, updatedUser.getEmail(), "Некорректный email");
            Assertions.assertEquals(login, updatedUser.getLogin(), "Некорректный логин");
            Assertions.assertEquals(name, updatedUser.getName(), "Некорректное имя");
            Assertions.assertEquals(birthday, updatedUser.getBirthday(), "Некорректная дата рождения");
        });
    }

    @Test
    public void whenUser2AddedToFriendUser1ThenReturnFriendship() {
        List<User> emptyFriends = userStorage.findFriends(2L);
        Assertions.assertTrue(emptyFriends.isEmpty(), "У пользователя не должно быть друзей");

        userStorage.addFriend(2L, 1L);

        List<User> friends = userStorage.findFriends(2L);

        Assertions.assertEquals(1, friends.size(), "Некорректное количество друзей в списке");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, friends.getFirst().getId(),
                    "Некорректный идентификатор пользователя");
            Assertions.assertEquals("test@test.ru", friends.getFirst().getEmail(),
                    "Некорректный email");
            Assertions.assertEquals("test123", friends.getFirst().getLogin(), "Некорректный логин");
            Assertions.assertEquals("John", friends.getFirst().getName(), "Некорректное имя");
            Assertions.assertEquals("1995-07-01", friends.getFirst().getBirthday().toString(),
                    "Некорректная дата рождения");
        });
    }

    @Test
    public void whenUser1DeletedUser2FromFriendsThenReturnBrokenFriendship() {
        userStorage.deleteFriend(1L, 2L);

        List<User> friends = userStorage.findFriends(1L);

        Assertions.assertTrue(friends.isEmpty(), "У пользователя не должно быть друзей");
    }

    @Test
    public void whenFindCommonFriendWithUser2ThenReturn1Friend() {
        User user = new User();
        String email = "newEmail@email.com";
        String login = "newLogin12345";
        String name = "John Doe";
        LocalDate birthday = LocalDate.parse("1995-07-03");
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);

        userStorage.saveUser(user);
        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(2L, 3L);

        List<User> commonFriends = userStorage.findCommonFriends(1L, 2L);

        Assertions.assertEquals(1, commonFriends.size(), "Некорректное количество общих друзей");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(3L, commonFriends.getFirst().getId(),
                    "Некорректный идентификатор пользователя");
            Assertions.assertEquals(email, commonFriends.getFirst().getEmail(),
                    "Некорректный email");
            Assertions.assertEquals(login, commonFriends.getFirst().getLogin(), "Некорректный логин");
            Assertions.assertEquals(name, commonFriends.getFirst().getName(), "Некорректное имя");
            Assertions.assertEquals(birthday, commonFriends.getFirst().getBirthday(),
                    "Некорректная дата рождения");
        });
    }
}
