package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int generator = 0;

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public void add(int id, Film film) {
        film.setId(++generator);
        films.put(film.getId(), film);
    }

    @Override
    public void put(int id, Film film) {
        if (films.get(film.getId()) == null) {
            throw new ValidationException("Фильм не найден");
        }
        films.put(id, film);
    }

    @Override
    public void remove(int id) {
        films.remove(id);
    }
}
