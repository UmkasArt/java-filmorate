package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    public static final int MAX_DESC_SIZE = 200;
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static int generator = 0;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество постов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Фильм: {}", film);
        isValidFilm(film);
        if (film.getId() == 0) {
            film.setId(++generator);
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.debug("Фильм: {}", film);
        isValidFilm(film);
        if (films.get(film.getId()) == null) {
            throw new ValidationException("Фильм не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    void isValidFilm(Film film) {
        if (film.getDescription().length() > MAX_DESC_SIZE) {
            throw new ValidationException("Длина описания больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата выхода фильма раньше дня рождения кино");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Невалидная длительность");
        }
    }
}
