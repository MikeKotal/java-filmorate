package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<MpaDto> getAllMpa() {
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public MpaDto getMpaById(@PathVariable @Positive Long id) {
        return mpaService.getMpaById(id);
    }
}
