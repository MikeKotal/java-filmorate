package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film mapToFilm(FilmRequest request) {
        log.info("FilmRequest в маппер: {}", request);
        Film film = new Film();
        film.setId(request.getId());
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setGenres(request.getGenres());
        film.setMpa(request.getMpa());
        log.info("Film из маппера: {}", film);
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        log.info("Film в маппер: {}", film);
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setLikes(film.getLikes());
        filmDto.setGenres(film.getGenres());
        filmDto.setMpa(film.getMpa());
        log.info("FilmDto из маппера: {}", filmDto);
        return filmDto;
    }
}
