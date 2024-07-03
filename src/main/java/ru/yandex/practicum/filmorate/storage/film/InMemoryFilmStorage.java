package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long counter = 0L;

    @Override
    public Collection<Film> findFilms() {
        Collection<Film> result = films.values();
        log.info("Films {}", result);
        return result;
    }

    @Override
    public Film findFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Фильма с таким id = %s не найдено", id));
        }
        return films.get(id);
    }

    @Override
    public Film addFilm(Film newFilm) {
        log.info("Add film {}", newFilm);
        newFilm.setId(++counter);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Update film {}", newFilm);
        if (newFilm.getId() == null) {
            ValidationException validationException = new ValidationException("Поле id не должно быть пустым");
            log.error(validationException.getMessage(), validationException);
            throw validationException;
        }
        if (!films.containsKey(newFilm.getId())) {
            NotFoundException notFoundException = new NotFoundException(String.format("Фильма с таким id = %s не найдено",
                    newFilm.getId()));
            log.error(notFoundException.getMessage(), notFoundException);
            throw notFoundException;
        }
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Updated film {}", oldFilm);
        return oldFilm;
    }
}
