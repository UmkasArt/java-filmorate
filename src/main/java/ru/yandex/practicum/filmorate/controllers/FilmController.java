package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    final static int MAX_DESC_SIZE = 200;
    final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static int generator = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество постов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Фильм: {}", film);
        if (!isValidFilm(film)) {
            throw new ValidationException();
        }
        if (film.getId() == 0) {
            film.setId(++generator);
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.debug("Фильм: {}", film);
        if (!isValidFilm(film)) {
            throw new ValidationException();
        }
        if (films.get(film.getId()) == null) {
            throw new ValidationException();
        }
        films.put(film.getId(), film);
        return film;
    }

    boolean isValidFilm(Film film) {
        if (film.getDescription().length() > MAX_DESC_SIZE) {
            return false;
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            return false;
        }
        if (film.getDuration() < 1) {
            return false;
        }
        return true;
    }
}
