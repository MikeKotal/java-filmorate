package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> findGenres();

    List<Genre> findGenresByFilmId(Long id);

    Optional<Genre> findGenreById(Long id);

    void addFilmGenre(Long filmId, Long genreId);

    void updateFilmGenre(Long filmId, Long genreId);
}
