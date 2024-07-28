package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
@NotNull(message = "Необходимо передать информацию о пользователе")
public class UserRequest {
    private Long id;
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Email имеет некорректный формат")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
