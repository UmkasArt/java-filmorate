package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    public Map<Integer, Film> getFilmsMap() {
        return inMemoryFilmStorage.getFilms();
    }

    public Film create(Film film) {
        isValidFilm(film);
        inMemoryFilmStorage.add(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        if (!inMemoryFilmStorage.getFilms().containsKey(film.getId())) {
            throw new NoSuchElementException();
        }
        isValidFilm(film);
        inMemoryFilmStorage.put(film.getId(), film);
        return film;
    }

    public void isValidFilm(Film film) {
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
