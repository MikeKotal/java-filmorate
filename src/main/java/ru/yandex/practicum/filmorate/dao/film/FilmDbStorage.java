package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DbStorage;
import ru.yandex.practicum.filmorate.dao.film.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends DbStorage<Film> implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Film> findFilms() {
        String query = """
                SELECT * FROM movies
                LEFT JOIN ratings ON movies.rating_id=ratings.rating_id
                """;
        return findMany(query);
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String query = """
                SELECT * FROM movies
                LEFT JOIN ratings ON movies.rating_id=ratings.rating_id
                WHERE movies.film_id = ?
                """;
        return findOne(query, id);
    }

    @Override
    public Film addFilm(Film newFilm) {
        String query = "INSERT INTO movies(name, description, release_date, duration) VALUES(?, ?, ?, ?)";
        Long id = insert(
                query,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration()
        );
        newFilm.setId(id);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        String query = "UPDATE movies SET name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?";
        update(
                query,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getId()
        );
        return newFilm;
    }

    @Override
    public void addLike(Long userId, Long filmId) {
        String query = "MERGE INTO likes(user_id, film_id) VALUES(?, ?)";
        String updateQuery = "UPDATE movies SET total_likes = total_likes + 1 WHERE film_id = ?";
        insertWithoutKey(query, userId, filmId);
        update(updateQuery, filmId);
    }

    @Override
    public void removeLike(Long userId, Long filmId) {
        String query = "DELETE FROM likes WHERE user_id = ?";
        String updateQuery = "UPDATE movies SET total_likes = total_likes - 1 WHERE film_id = ?";
        delete(query, userId);
        update(updateQuery, filmId);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        String query = """
                SELECT * FROM movies
                LEFT JOIN ratings ON movies.rating_id=ratings.rating_id
                ORDER BY total_likes DESC
                LIMIT ?
                """;
        return findMany(query, count);
    }
}
