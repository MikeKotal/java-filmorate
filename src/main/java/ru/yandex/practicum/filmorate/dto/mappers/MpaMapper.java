package ru.yandex.practicum.filmorate.dto.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static MpaDto mapToMpaDto(Mpa mpa) {
        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());
        log.info("MpaDto из маппера: {}", mpaDto);
        return mpaDto;
    }
}
