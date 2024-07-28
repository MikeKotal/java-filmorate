package ru.yandex.practicum.filmorate.controller.dao.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dao.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao.genre")
public class GenreDbStorageTests {

    private final GenreDbStorage genreDbStorage;

    @Test
    public void whenFindAllGenresThenReturnArrayWithLength6() {
        List<Genre> genres = genreDbStorage.findGenres();

        Assertions.assertEquals(6, genres.size(), "Некорректное количество жанров в массиве");
    }

    @Test
    public void whenFindGenreByFilmId1ThenReturnFilmGenreAction6() {
        List<Genre> genres = genreDbStorage.findGenresByFilmId(1L);

        Assertions.assertEquals(1, genres.size(), "Некорректное количество жанров у фильма");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(6L, genres.getFirst().getId(), "Некорректный идентификатор");
            Assertions.assertEquals("Боевик", genres.getFirst().getName(), "Некорректный жанр");
        });
    }

    @Test
    public void whenFindGenreById1ThenReturnComedyWitIdIs1() {
        Genre currentGenre = genreDbStorage.findGenreById(1L).orElse(null);

        Assertions.assertNotNull(currentGenre, "Жанр не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, currentGenre.getId(), "Некорректный идентификатор жанра");
            Assertions.assertEquals("Комедия", currentGenre.getName(), "Некорректно имя жанра");
        });
    }

    @Test
    public void whenAddGenreByFilmThenFilmHasNewGenre() {
        genreDbStorage.addFilmGenre(1L, 1L);
        List<Genre> genres = genreDbStorage.findGenresByFilmId(1L);

        Assertions.assertEquals(2, genres.size(), "Некорректное количество жанрова у фильма");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, genres.getLast().getId(), "Некорректный идентификатор");
            Assertions.assertEquals("Комедия", genres.getLast().getName(), "Некорректный жанр");
        });
    }

    @Test
    public void whenUpdateFilmGenreThenReturnNewFilmGenre() {
        genreDbStorage.updateFilmGenre(1L, 1L);
        List<Genre> genres = genreDbStorage.findGenresByFilmId(1L);

        Assertions.assertEquals(1, genres.size(), "Некорректное количество жанров у фильма");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1L, genres.getFirst().getId(), "Некорректный идентификатор");
            Assertions.assertEquals("Комедия", genres.getFirst().getName(), "Некорректный жанр");
        });
    }
}
