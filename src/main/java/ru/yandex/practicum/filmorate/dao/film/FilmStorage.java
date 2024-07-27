package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findFilms();

    Optional<Film> findFilmById(Long id);

    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    List<Long> findUsersIdLikes(Long filmId);

    void addLike(Long userId, Long filmId);

    void removeLike(Long userId, Long filmId);

    List<Film> findPopularFilms(Integer count);
}
