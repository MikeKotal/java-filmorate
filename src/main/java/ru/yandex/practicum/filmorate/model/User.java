package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"email"})
@NotNull(message = "Необходимо передать информацию о пользователе")
public class User {
    private Long id;
    @NotNull(message = "Email не должен быть пустым")
    @Email(message = "Email имеет некорректный формат")
    private String email;
    @NotNull(message = "Логин не должен быть пустым")
    @NotBlank(message = "Логин состоит только из пробелов")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
