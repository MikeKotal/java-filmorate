package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.findFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(required = false) Long id) {
        return filmStorage.findFilmById(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film newFilm) {
        return filmStorage.addFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        checkEmptyFields(id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        checkEmptyFields(id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10") Long count) {
        if (count <= 0) {
            throw new ValidationException("Передано некорректное значение для выборки популярных фильмов");
        }
        return filmService.findPopularFilms(count);
    }

    private void checkEmptyFields(Long firstId, Long secondId) {
        if (firstId <= 0 || secondId <= 0) {
            throw new ValidationException("В строке запроса было передано отрицательное значение");
        }
    }
}
