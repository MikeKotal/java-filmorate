package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> findAllMpa();

    Mpa findMpaByFilmId(Long id);

    Optional<Mpa> findMpaById(Long id);

    void addFilmMpa(Long filmId, Long ratingId);

    void updateFilmMpa(Long filmId, Long ratingId);
}
