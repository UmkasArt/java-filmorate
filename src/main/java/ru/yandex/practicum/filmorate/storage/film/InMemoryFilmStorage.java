package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int generator = 0;

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

    @Override
    public void add(int id, Film film) {
        if (film.getId() == 0) {
            film.setId(++generator);
        }
        films.put(film.getId(), film);
    }
}
