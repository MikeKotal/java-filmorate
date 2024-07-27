package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> getGenres() {
        List<Genre> genres = genreStorage.findGenres();
        log.info("Все жанры {}", genres);
        return genres;
    }

    public Genre getGenreById(Long id) {
        log.info("Запрос на поиска жанра с id {}", id);
        return genreStorage.findGenreById(id).orElseThrow(() -> {
            log.error("Жанра с id {} нет", id);
            return new NotFoundException("Жанра с таким идентификатором нет");
        });
    }
}
