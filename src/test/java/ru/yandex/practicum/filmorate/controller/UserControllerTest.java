package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private Validator validator;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void checkSuccessUserValidation() {
        user.setId(1L);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("Test");
        user.setBirthday(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<User>> result = validator.validate(user);
        assertTrue(result.isEmpty(), "Ошибки по валидации быть не должно");
    }

    @Test
    public void whenEmailIsNullThenValidationIsFailed() {
        user.setId(1L);
        user.setEmail("");
        user.setLogin("login");
        user.setName("Test");
        user.setBirthday(LocalDate.now().minusDays(1));

        List<ConstraintViolation<User>> result = List.copyOf(validator.validate(user));
        assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

        ConstraintViolation<User> validationResult = result.getFirst();

        assertEquals("email", validationResult.getPropertyPath().toString());
        assertEquals("Email не должен быть пустым", validationResult.getMessage());
    }

    @Test
    public void whenEmailMaskIsInvalidThenValidationIsFailed() {
        user.setId(1L);
        user.setEmail("@test.test");
        user.setLogin("login");
        user.setName("Test");
        user.setBirthday(LocalDate.now().minusDays(1));

        List<ConstraintViolation<User>> result = List.copyOf(validator.validate(user));
        assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

        ConstraintViolation<User> validationResult = result.getFirst();

        assertEquals("email", validationResult.getPropertyPath().toString());
        assertEquals("Email имеет некорректный формат", validationResult.getMessage());
    }

    @Test
    public void whenLoginIsNullThenValidationIsFailed() {
        user.setId(1L);
        user.setEmail("test@test.ru");
        user.setLogin("");
        user.setName("Test");
        user.setBirthday(LocalDate.now().minusDays(1));

        List<ConstraintViolation<User>> result = List.copyOf(validator.validate(user));
        assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

        ConstraintViolation<User> validationResult = result.getFirst();

        assertEquals("login", validationResult.getPropertyPath().toString());
        assertEquals("Логин не должен быть пустым", validationResult.getMessage());
    }

    @Test
    public void whenBirthdayInFutureThenValidationIsFailed() {
        user.setId(1L);
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("Test");
        user.setBirthday(LocalDate.now().plusDays(1));

        List<ConstraintViolation<User>> result = List.copyOf(validator.validate(user));
        assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

        ConstraintViolation<User> validationResult = result.getFirst();

        assertEquals("birthday", validationResult.getPropertyPath().toString());
        assertEquals("Дата рождения не может быть в будущем", validationResult.getMessage());
    }
}
