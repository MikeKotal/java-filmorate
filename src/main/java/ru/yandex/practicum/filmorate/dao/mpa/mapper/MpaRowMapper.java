package ru.yandex.practicum.filmorate.dao.mpa.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("rating_id"));
        mpa.setName(rs.getString("name"));
        log.info("Дессериализованный рейтинг из базы {}", mpa);
        return mpa;
    }
}
