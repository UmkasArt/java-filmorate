package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;

    private static final int MAX_DESC_SIZE = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public List<Film> getAllFilms() {
        log.debug("Текущее количество постов: {}", inMemoryFilmStorage.getFilms().size());
        return new ArrayList<>(inMemoryFilmStorage.getFilms().values());
    }

    private Map<Integer, Film> getFilmsMap() {
        return inMemoryFilmStorage.getFilms();
    }

    public Film create(@NotNull Film film) {
        if (film.getId() != 0) {
            throw new IllegalArgumentException();
        }
        validateFilm(film);
        inMemoryFilmStorage.add(film.getId(), film);
        return film;
    }

    public Film findFilmById(int filmId) {
        if (!getFilmsMap().containsKey(filmId)) {
            throw new NoSuchElementException();
        }
        return getFilmsMap().get(filmId);
    }

    public void putLike(int id, int userId) {
        if (!getFilmsMap().containsKey(id) || userId <= 0) {
            throw new NoSuchElementException();
        }
        getFilmsMap().get(id).getLikesSet().add(userId);
    }

    public void deleteLike(int id, int userId) {
        if (!getFilmsMap().containsKey(id) || userId <= 0) {
            throw new NoSuchElementException();
        }
        getFilmsMap().get(id).getLikesSet().remove(userId);
    }

    public List<Film> getOrderFilm(int count) {
        if (count <= 0) {
            throw new ValidationException("Некорретный атрибут count");
        }
        return getAllFilms().stream().sorted((f0, f1) ->
                f1.getLikesSet().size() - f0.getLikesSet().size()).limit(count).collect(Collectors.toList());
    }

    public Film put(@NotNull Film film) {
        if (!inMemoryFilmStorage.getFilms().containsKey(film.getId())) {
            throw new NoSuchElementException();
        }
        validateFilm(film);
        inMemoryFilmStorage.put(film.getId(), film);
        return film;
    }

    public void validateFilm(@NotNull Film film) {
        if (film.getDescription().length() > MAX_DESC_SIZE) {
            throw new ValidationException("Description can not be more than 200 characters");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("The release date of the film before the birthday of the movie");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Invalid duration");
        }
    }
}
