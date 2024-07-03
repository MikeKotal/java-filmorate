package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public void addLike(Long id, Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(id);
        Set<Long> userLikes = film.getUserIdLikes() == null ? new HashSet<>() : film.getUserIdLikes();
        if (userLikes.contains(user.getId())) {
            throw new ConditionsNotMetException("Пользователь может поставить лайк фильму только один раз");
        }
        userLikes.add(user.getId());
        film.setUserIdLikes(userLikes);
        log.info("Film: {} added like by user: {}", film, user);
    }

    public void removeLike(Long id, Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(id);
        Set<Long> userLikes = film.getUserIdLikes() == null ? new HashSet<>() : film.getUserIdLikes();
        if (!userLikes.contains(user.getId())) {
            throw new NotFoundException("У фильма отсутствует лайк данного пользователя");
        }
        userLikes.remove(user.getId());
        film.setUserIdLikes(userLikes);
        log.info("Film: {} deleted like by user: {}", film, user);
    }

    public Collection<Film> findPopularFilms(Long count) {
        Collection<Film> films = filmStorage.findFilms()
                .stream()
                .filter(film -> film.getLikes() != null)
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(count)
                .toList();
        log.info("Popular films {}", films);
        return films;
    }
}
