package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"email"})
@NotNull(message = "Необходимо передать информацию о пользователе")
public class User {
    private Long id;
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Email имеет некорректный формат")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friendIds = new HashSet<>();
}
