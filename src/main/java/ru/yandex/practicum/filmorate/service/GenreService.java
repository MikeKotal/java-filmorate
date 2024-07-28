package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<GenreDto> getGenres() {
        List<GenreDto> genres = genreStorage.findGenres()
                .stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
        log.info("Все жанры {}", genres);
        return genres;
    }

    public GenreDto getGenreById(Long id) {
        log.info("Запрос на поиска жанра с id {}", id);
        return genreStorage.findGenreById(id).map(GenreMapper::mapToGenreDto).orElseThrow(() -> {
            log.error("Жанра с id {} нет", id);
            return new NotFoundException("Жанра с таким идентификатором нет");
        });
    }
}
