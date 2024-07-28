package ru.yandex.practicum.filmorate.controller.dao.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao.mpa")
public class MpaDbStorageTests {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void whenFindAllMpaThenReturnArrayWithLength5() {
        List<Mpa> mpa = mpaDbStorage.findAllMpa();

        Assertions.assertEquals(5, mpa.size(), "Некорректное количество рейтингов в массиве");
    }

    @Test
    public void whenFindMpaByFilmId1ThenReturnR() {
        Mpa mpa = mpaDbStorage.findMpaByFilmId(1L);

        Assertions.assertNotNull(mpa, "Рейтинг не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(4, mpa.getId(), "Некорректный идентификатор у рейтинга");
            Assertions.assertEquals("R", mpa.getName(), "Некорректное наименование рейтинга");
        });
    }

    @Test
    public void whenFindMpaById1ThenReturnG() {
        Mpa mpa = mpaDbStorage.findMpaById(1L).orElse(null);

        Assertions.assertNotNull(mpa, "Рейтинг не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1, mpa.getId(), "Некорректный идентификатор у рейтинга");
            Assertions.assertEquals("G", mpa.getName(), "Некорректное наименование рейтинга");
        });
    }

    @Test
    public void whenAddMpaRatingByFilmId2WithMpa2ThenReturnMpaPG() {
        mpaDbStorage.updateFilmMpa(2L, 2L);

        Mpa mpa = mpaDbStorage.findMpaById(2L).orElse(null);
        Assertions.assertNotNull(mpa, "Рейтинг не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(2, mpa.getId(), "Некорректный идентификатор у рейтинга");
            Assertions.assertEquals("PG", mpa.getName(), "Некорректное наименование рейтинга");
        });
    }

    @Test
    public void whenUpdateFilmId1WithMpa4ToMpa1ThenReturnMpaG() {
        mpaDbStorage.updateFilmMpa(1L, 1L);

        Mpa mpa = mpaDbStorage.findMpaById(1L).orElse(null);
        Assertions.assertNotNull(mpa, "Рейтинг не должен быть пустым");
        Assertions.assertAll(() -> {
            Assertions.assertEquals(1, mpa.getId(), "Некорректный идентификатор у рейтинга");
            Assertions.assertEquals("G", mpa.getName(), "Некорректное наименование рейтинга");
        });
    }
}
