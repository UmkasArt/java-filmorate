package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    void put(int id, Film film);

    void add(int id, Film film);

    void remove(int id);
}
