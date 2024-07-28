package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<MpaDto> getAllMpa() {
        List<MpaDto> mpa = mpaStorage.findAllMpa()
                .stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
        log.info("Список рейтингов {}", mpa);
        return mpa;
    }

    public MpaDto getMpaById(Long id) {
        return mpaStorage.findMpaById(id).map(MpaMapper::mapToMpaDto).orElseThrow(() -> {
            log.error("Рейтинга с id {} нет", id);
            return new NotFoundException("Рейтинга с таким идентификатором нет");
        });
    }
}
