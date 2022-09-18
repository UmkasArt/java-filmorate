package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> getFilmsStorage();

    void addFilm(Integer id, Film film);

    void putFilm(Integer id, Film film);

    void removeFilm(Integer id);

    List<Film> getOrderByLikeFilms(Integer count);

    void putLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);
}
