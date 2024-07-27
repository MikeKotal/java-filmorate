package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DbStorage;
import ru.yandex.practicum.filmorate.dao.mpa.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorage extends DbStorage<Mpa> implements MpaStorage {

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<Mpa> findAllMpa() {
        String query = "SELECT * FROM ratings";
        return findMany(query);
    }

    @Override
    public Mpa findMpaByFilmId(Long id) {
        String query = """
                SELECT r.rating_id, r.name
                FROM film_ratings AS fr
                LEFT JOIN ratings AS r ON fr.rating_id=r.rating_id
                WHERE fr.film_id = ?
                """;
        return findOne(query, id).orElse(null);
    }

    @Override
    public Optional<Mpa> findMpaById(Long id) {
        String query = "SELECT * FROM ratings WHERE rating_id = ?";
        return findOne(query, id);
    }

    @Override
    public void addFilmMpa(Long filmId, Long ratingId) {
        String query = "INSERT INTO film_ratings(film_id, rating_id) VALUES(?, ?)";
        insert(query, filmId, ratingId);
    }

    @Override
    public void updateFilmMpa(Long filmId, Long ratingId) {
        String query = "UPDATE film_ratings SET rating_id = ? WHERE film_id = ?";
        update(query, ratingId, filmId);
    }
}
