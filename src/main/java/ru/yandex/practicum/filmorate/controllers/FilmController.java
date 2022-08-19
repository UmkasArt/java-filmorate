package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Фильм: {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.debug("Фильм: {}", film);
        return filmService.put(film);
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable int filmId) {
        if (!filmService.getFilmsMap().containsKey(filmId)) {
            throw new NoSuchElementException();
        }
        return filmService.getFilmsMap().get(filmId);
    }

    @PutMapping("{id}/like/{userId}")
    public void putLike(@PathVariable int id, @PathVariable int userId) {
        if (!filmService.getFilmsMap().containsKey(id) || userId <= 0) {
            throw new NoSuchElementException();
        }
        filmService.getFilmsMap().get(id).getLikesSet().add(userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        if (!filmService.getFilmsMap().containsKey(id) || userId <= 0) {
            throw new NoSuchElementException();
        }
        filmService.getFilmsMap().get(id).getLikesSet().remove(userId);
    }

    @GetMapping("/popular")
    public List<Film> getOrderFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new ValidationException("Некорретный атрибут count");
        }
        return filmService.getAllFilms().stream().sorted((f0, f1) ->
                f1.getLikesSet().size() - f0.getLikesSet().size()).limit(count).collect(Collectors.toList());
    }
}
