package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findFilms();

    Film findFilmById(Long id);

    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);
}
