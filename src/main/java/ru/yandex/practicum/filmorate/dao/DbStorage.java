package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class DbStorage<T> {

    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbcTemplate.queryForObject(query, mapper, params);
            log.info("Получен объект {} по запросу {}", result, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            log.error("По запросу {} что-то пошло не так, возвращаем пустой ответ", params);
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbcTemplate.query(query, mapper, params);
    }

    protected void delete(String query, Object... params) {
        int rowsDeleted = jdbcTemplate.update(query, params);
        log.info("Строк удалено {} для сущности {}", rowsDeleted, params);
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbcTemplate.update(query, params);
        log.info("Строк изменено {} для сущности {}", rowsUpdated, params);
        if (rowsUpdated == 0) {
            log.error("Ошибка при обновлении: {}", params);
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    protected Long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int id = 0; id < params.length; id++) {
                ps.setObject(id + 1, params[id]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        log.info("Идентификатор созданной записи {}, сущность {}", params, query);
        return id;
    }

    protected void insertWithoutKey(String query, Object... params) {
        int rowsCreated = jdbcTemplate.update(query, params);
        log.info("Строк добавлено {} для сущности {}", rowsCreated, params);
        if (rowsCreated == 0) {
            log.error("Ошибка при создании: {}", params);
            throw new InternalServerException("Не удалось создать данные");
        }
    }
}
