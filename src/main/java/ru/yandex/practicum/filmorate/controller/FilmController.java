package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        Collection<Film> result = films.values();
        log.info("Films {}", result);
        return result;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film newFilm) {
        log.info("Add film {}", newFilm);
        newFilm.setId(getNewId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Update film {}", newFilm);
        if (newFilm.getId() == null) {
            ValidationException validationException = new ValidationException("Поле id не должно быть пустым");
            log.error(validationException.getMessage(), validationException);
            throw validationException;
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Updated film {}", oldFilm);
            return oldFilm;
        }
        NotFoundException notFoundException = new NotFoundException(String.format("Фильма с таким id = %s не найдено",
                newFilm.getId()));
        log.error(notFoundException.getMessage(), notFoundException);
        throw notFoundException;
    }

    private Long getNewId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
