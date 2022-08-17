package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> getFilms();

    void add(int id, Film film);

    void put(int id, Film film);

    void remove(int id);
}
