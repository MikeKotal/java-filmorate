package ru.yandex.practicum.filmorate.dao.film.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getLong("duration"));
        film.setLikes(rs.getInt("total_likes"));

        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("ratings.rating_id"));
        mpa.setName(rs.getString("ratings.name"));
        log.info("Дессериализованный рейтинг {}", mpa);
        film.setMpa(mpa);

        log.info("Дессериализованный фильм из базы: {}", film);
        return film;
    }
}
