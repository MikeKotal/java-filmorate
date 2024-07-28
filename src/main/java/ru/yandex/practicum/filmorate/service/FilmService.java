package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.dto.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public List<FilmDto> getFilms() {
        List<FilmDto> films = filmStorage.findFilms()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
        log.info("Все фильмы: {}", films);
        return films;
    }

    public FilmDto getFilmById(Long id) {
        FilmDto filmDto = FilmMapper.mapToFilmDto(getFilm(id));
        filmDto.setGenres(genreStorage.findGenresByFilmId(filmDto.getId()));
        log.info("Фильм с id '{}' = '{}'", id, filmDto);
        return filmDto;
    }

    public FilmDto saveFilm(FilmRequest request) {
        log.info("Запрос на сохранение фильма {}", request);
        Film newFilm = FilmMapper.mapToFilm(request);
        List<Genre> genres = getSimilarGenres(newFilm.getGenres());
        Mpa mpa = getSimilarMpa(newFilm.getMpa());
        newFilm = filmStorage.addFilm(newFilm);
        Long filmId = newFilm.getId();
        if (!genres.isEmpty()) {
            genres.forEach(genre -> genreStorage.addFilmGenre(filmId, genre.getId()));
            newFilm.setGenres(genres);
        }
        if (mpa != null) {
            mpaStorage.updateFilmMpa(filmId, mpa.getId());
            newFilm.setMpa(mpa);
        }
        log.info("Фильм сохранен {}", newFilm);
        return FilmMapper.mapToFilmDto(newFilm);
    }

    public FilmDto updateFilm(FilmRequest request) {
        log.info("Запрос на обновление фильма {}", request);
        Film newFilm = FilmMapper.mapToFilm(request);
        if (newFilm.getId() == null) {
            log.error("Отсутствует идентификатор фильма при обновлении");
            throw new ValidationException("Не был передан идентификатор фильма");
        }
        if (newFilm.getId() <= 0) {
            log.error("Неверно передан идентификатор фильма {}", newFilm.getId());
            throw new ValidationException("Идентификатор фильма не должен быть 0 или отрицательным");
        }
        Film oldFilm = getFilm(newFilm.getId());
        List<Genre> genres = getSimilarGenres(newFilm.getGenres());
        Mpa mpa = getSimilarMpa(newFilm.getMpa());
        newFilm = filmStorage.updateFilm(newFilm);
        Long filmId = newFilm.getId();
        if (!genres.isEmpty()) {
            genres.forEach(genre -> genreStorage.updateFilmGenre(filmId, genre.getId()));
            newFilm.setGenres(genres);
        }
        if (mpa != null) {
            mpaStorage.updateFilmMpa(filmId, mpa.getId());
            newFilm.setMpa(mpa);
        }
        newFilm.setLikes(oldFilm.getLikes());
        log.info("Фильм обновлен {}", newFilm);
        return FilmMapper.mapToFilmDto(newFilm);
    }

    public void addLike(Long id, Long userId) {
        log.info("Запрос на добавление лайка фильму user = {}, film = {}", userId, id);
        checkEmptyFields(id, userId);
        User user = findUserById(userId);
        Film film = getFilm(id);
        filmStorage.addLike(user.getId(), film.getId());
        log.info("Фильму: {} добавлен лайк от пользователя: {}", film, user);
    }

    public void removeLike(Long id, Long userId) {
        log.info("Запрос на удаление лайка у фильма user = {}, film = {}", id, userId);
        checkEmptyFields(id, userId);
        User user = findUserById(userId);
        Film film = getFilm(id);
        filmStorage.removeLike(user.getId(), film.getId());
        log.info("У фильма: {} удален лайк пользователя: {}", film, user);
    }

    public List<FilmDto> findPopularFilms(Integer count) {
        log.info("Запрос на выборку фильмов count = {}", count);
        if (count <= 0) {
            log.error("Некорректное значение выборки популярных фильмов {}", count);
            throw new ValidationException("Передано некорректное значение для выборки популярных фильмов");
        }
        List<FilmDto> films = filmStorage.findPopularFilms(count)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
        log.info("Топ {} популярных фильмов {}", count, films);
        return films;
    }

    private void checkEmptyFields(Long firstId, Long secondId) {
        if (firstId <= 0 || secondId <= 0) {
            log.error("Некорректные значения в URL {}, {}", firstId, secondId);
            throw new ValidationException("В строке запроса было передано отрицательное значение");
        }
    }

    private User findUserById(Long id) {
        return userStorage.findUserById(id).orElseThrow(() -> {
            log.error("Такого пользователя нет {}", id);
            return new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", id));
        });
    }

    private Film getFilm(Long id) {
        return filmStorage.findFilmById(id).orElseThrow(() -> {
            log.error("Такого фильма нет {}", id);
            return new NotFoundException(String.format("Фильма с идентификатором = '%s' не найдено", id));
        });
    }

    private List<Genre> getSimilarGenres(List<Genre> genres) {
        if (genres == null) {
            log.error("Список жанров на входе пустой");
            return new ArrayList<>();
        }
        List<Genre> similarGenres = genreStorage.findGenres()
                .stream()
                .filter(dbGenre -> genres.stream().anyMatch(genre -> genre.equals(dbGenre)))
                .toList();
        if (similarGenres.isEmpty()) {
            log.error("Жанров с такими идентификаторами нет {}", genres);
            throw new ValidationException("Жанров с такими идентификаторами нет");
        }
        log.info("Совпадающие жанры фильмов и в базе {}", similarGenres);
        return similarGenres;
    }

    private Mpa getSimilarMpa(Mpa mpa) {
        if (mpa == null) {
            log.error("Рейтинг фильма на входе пустой");
            return new Mpa();
        }
        Mpa resultMpa = mpaStorage.findAllMpa()
                .stream()
                .filter(rating -> rating.equals(mpa))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Был передан некорректный рейтинг {}", mpa);
                    return new ValidationException("Рейтинга с таким идентификатором нет");
                });
        log.info("Совпадающие рейтинги фильмов и в базе {}", resultMpa);
        return resultMpa;
    }
}
