package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer likes;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Genre> genres;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Mpa mpa;
}
