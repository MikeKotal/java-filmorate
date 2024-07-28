package ru.yandex.practicum.filmorate.controller.dao.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao.film")
public class FilmDbStorageTests {

    private final FilmDbStorage filmDbStorage;

    @Test
    public void whenFindAllFilmsThenReturnArrayLengthIs2() {
        List<Film> films = filmDbStorage.findFilms();

        Assertions.assertEquals(2, films.size(), "Некорректное количество фильмов в списке");
    }

    @Test
    public void whenFindFilmById1ThenReturnFilmWithId1() {
        Film film = filmDbStorage.findFilmById(1L).orElse(null);

        Assertions.assertNotNull(film, "Фильм не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, film.getId(), "Некорректный идентификатор фильма");
            Assertions.assertEquals("Фильм", film.getName(), "Некорректное название");
            Assertions.assertEquals("Крутой фильм", film.getDescription(), "Некорректное описание");
            Assertions.assertEquals("2024-07-01", film.getReleaseDate().toString(),
                    "Некорректная дата релиза");
            Assertions.assertEquals(150L, film.getDuration(), "Некорректная продолжительность");
            Assertions.assertEquals(1, film.getLikes(), "Некорректно количество лайков");
            Assertions.assertNotNull(film.getMpa(), "У фильма должен быть рейтинг");
        });
    }

    @Test
    public void whenAddNewFilmThenNewFilmReturnWithId3() {
        Film film = new Film();
        String name = "Новый фильм";
        String description = "Новое описание";
        LocalDate releaseDate = LocalDate.parse("2024-07-03");
        Long duration = 180L;
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);

        filmDbStorage.addFilm(film);

        Film createdFilm = filmDbStorage.findFilmById(3L).orElse(null);

        Assertions.assertNotNull(createdFilm, "Фильм не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(3L, createdFilm.getId(), "Некорректный идентификатор фильма");
            Assertions.assertEquals(name, createdFilm.getName(), "Некорректное название");
            Assertions.assertEquals(description, createdFilm.getDescription(), "Некорректное описание");
            Assertions.assertEquals(releaseDate, createdFilm.getReleaseDate(), "Некорректная дата релиза");
            Assertions.assertEquals(duration, createdFilm.getDuration(), "Некорректная продолжительность");
            Assertions.assertEquals(0, createdFilm.getLikes(), "Некорректно количество лайков");
        });
    }

    @Test
    public void whenUpdateFilmWithId1ThenFilmReturnUpdated() {
        Film film = new Film();
        Long id = 1L;
        String name = "Новый фильм";
        String description = "Новое описание";
        LocalDate releaseDate = LocalDate.parse("2024-07-03");
        Long duration = 180L;
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);

        filmDbStorage.updateFilm(film);

        Film updatedFilm = filmDbStorage.findFilmById(1L).orElse(null);

        Assertions.assertNotNull(updatedFilm, "Фильм не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(id, updatedFilm.getId(), "Некорректный идентификатор фильма");
            Assertions.assertEquals(name, updatedFilm.getName(), "Некорректное название");
            Assertions.assertEquals(description, updatedFilm.getDescription(), "Некорректное описание");
            Assertions.assertEquals(releaseDate, updatedFilm.getReleaseDate(), "Некорректная дата релиза");
            Assertions.assertEquals(duration, updatedFilm.getDuration(), "Некорректная продолжительность");
            Assertions.assertEquals(1, updatedFilm.getLikes(), "Некорректно количество лайков");
            Assertions.assertNotNull(updatedFilm.getMpa(), "У фильма должен быть рейтинг");
        });
    }

    @Test
    public void whenUserAddLikeThenFilmLikeIncrease() {
        filmDbStorage.addLike(2L, 1L);

        Film film = filmDbStorage.findFilmById(1L).orElse(null);

        Assertions.assertNotNull(film, "Фильм не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, film.getId(), "Некорректный идентификатор фильма");
            Assertions.assertEquals(2, film.getLikes(), "Некорректно количество лайков");
        });
    }

    @Test
    public void whenUserDeleteLikeThenFilmLikeDecrease() {
        filmDbStorage.removeLike(1L, 1L);

        Film film = filmDbStorage.findFilmById(1L).orElse(null);

        Assertions.assertNotNull(film, "Фильм не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, film.getId(), "Некорректный идентификатор фильма");
            Assertions.assertEquals(0, film.getLikes(), "Некорректно количество лайков");
        });
    }

    @Test
    public void whenFindPopularFilmThenFilmReturnByDescSort() {
        filmDbStorage.addLike(2L, 1L);
        filmDbStorage.addLike(1L, 2L);

        List<Film> films = filmDbStorage.findPopularFilms(5);

        Assertions.assertEquals(2, films.size());
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, films.getFirst().getId(),
                    "Первый фильм в списке некорректный");
            Assertions.assertEquals(2, films.getFirst().getLikes(), "Количество лайков некорректно");
            Assertions.assertEquals(2L, films.getLast().getId(), "Последний фильм некорректный");
            Assertions.assertEquals(1, films.getLast().getLikes(), "Количество лайков некорректно");
        });
    }
}
