package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private static final int MAX_DESC_SIZE = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        log.debug("Текущее количество постов: {}", filmStorage.getFilmsStorage().size());
        return new ArrayList<>(filmStorage.getFilmsStorage().values());
    }

    private Map<Integer, Film> getFilmsMap() {
        return filmStorage.getFilmsStorage();
    }

    public Film createFilm(@NotNull Film film) {
        if (film.getId() != null) {
            throw new IllegalArgumentException();
        }
        validateFilm(film);
        filmStorage.addFilm(film.getId(), film);
        return getFilmsMap().get(film.getId());
    }

    public Film findFilmById(Integer filmId) {
        if (!getFilmsMap().containsKey(filmId)) {
            throw new NoSuchElementException();
        }
        return getFilmsMap().get(filmId);
    }

    public void putLike(Integer id, Integer userId) {
        if (!getFilmsMap().containsKey(id) || userId <= 0) {
            throw new NoSuchElementException();
        }
        filmStorage.putLike(id, userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        if (!getFilmsMap().containsKey(id) || userId <= 0) {
            throw new NoSuchElementException();
        }
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getOrderFilm(Integer count) {
        if (count <= 0) {
            throw new ValidationException("Некорретный атрибут count");
        }
        return filmStorage.getOrderByLikeFilms(count);
    }

    public Film putFilm(@NotNull Film film) {
        if (!filmStorage.getFilmsStorage().containsKey(film.getId())) {
            throw new NoSuchElementException();
        }
        validateFilm(film);
        filmStorage.putFilm(film.getId(), film);
        return getFilmsMap().get(film.getId());
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
        if (film.getMpa() == null) {
            throw new ValidationException("Invalid duration");
        }
    }
}
