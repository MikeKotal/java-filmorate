package ru.yandex.practicum.filmorate.dao.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DbStorage;
import ru.yandex.practicum.filmorate.dao.genre.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class GenreDbStorage extends DbStorage<Genre> implements GenreStorage {

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Genre> findGenres() {
        String query = "SELECT * FROM genres";
        return findMany(query);
    }

    @Override
    public List<Genre> findGenresByFilmId(Long id) {
        String query = """
                SELECT g.genre_id, g.name
                FROM film_genres AS fg
                LEFT JOIN genres AS g ON fg.genre_id=g.genre_id
                WHERE fg.film_id = ?
                """;
        return findMany(query, id);
    }

    @Override
    public Optional<Genre> findGenreById(Long id) {
        String query = "SELECT * FROM genres WHERE genre_id = ?";
        return findOne(query, id);
    }

    @Override
    public void addFilmGenre(Long filmId, Long genreId) {
        String query = "INSERT INTO film_genres(film_id, genre_id) VALUES(?, ?)";
        insert(query, filmId, genreId);
    }

    @Override
    public void updateFilmGenre(Long filmId, Long genreId) {
        String query = "UPDATE film_genres SET genre_id = ? WHERE film_id = ?";
        update(query, genreId, filmId);
    }
}
