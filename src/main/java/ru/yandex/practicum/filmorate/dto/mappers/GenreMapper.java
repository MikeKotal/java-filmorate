package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper {
    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        log.info("GenreDto из маппера: {}", genreDto);
        return genreDto;
    }
}
