package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"id"})
@NotNull(message = "Необходимо передать информацию о фильме")
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальное количество символов = 200")
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность не может быть отрицательной")
    private Long duration;
    private Set<Long> userIdLikes;
    private Integer likes;

    public void setUserIdLikes(Set<Long> userIdLikes) {
        this.userIdLikes = userIdLikes;
        this.likes = userIdLikes.size();
    }
}
