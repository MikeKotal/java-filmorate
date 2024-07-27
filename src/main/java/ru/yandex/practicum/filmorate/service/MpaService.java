package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        List<Mpa> mpa = mpaStorage.findAllMpa();
        log.info("Список рейтингов {}", mpa);
        return mpa;
    }

    public Mpa getMpaById(Long id) {
        return mpaStorage.findMpaById(id).orElseThrow(() -> {
            log.error("Рейтинга с id {} нет", id);
            return new NotFoundException("Рейтинга с таким идентификатором нет");
        });
    }
}
