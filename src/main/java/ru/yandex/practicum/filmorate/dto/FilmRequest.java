package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
@NotNull(message = "Необходимо передать информацию о фильме")
public class FilmRequest {
    private Long id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальное количество символов = 200")
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность не может быть отрицательной")
    private Long duration;
    private Mpa mpa;
    private List<Genre> genres;
}
