package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component("InMemoryStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private static int generatorId = 0;
    private final Map<Integer, Film> filmsStorage = new HashMap<>();
    private final Map<Integer, Set<Integer>> likesStorage = new HashMap<>();

    @Override
    public Map<Integer, Film> getFilmsStorage() {
        return filmsStorage;
    }

    @Override
    public void addFilm(Integer id, Film film) {
        film.setId(++generatorId);
        filmsStorage.put(film.getId(), film);
    }

    @Override
    public void putFilm(Integer id, Film film) {
        if (filmsStorage.get(film.getId()) == null) {
            throw new ValidationException("Фильм не найден");
        }
        filmsStorage.put(id, film);
    }

    @Override
    public void removeFilm(Integer id) {
        filmsStorage.remove(id);
    }


    @Override
    public List<Film> getOrderByLikeFilms(Integer count) {
        return likesStorage.keySet().
                stream()
                .sorted((i0, i1) -> likesStorage.get(i1).size() - likesStorage.get(i0).size())
                .limit(count)
                .collect(Collectors.toList()).stream().map(filmsStorage::get).collect(Collectors.toList());
    }


    @Override
    public void putLike(Integer filmId, Integer userId) {
        if (!likesStorage.containsKey(filmId)) {
            likesStorage.put(filmId, new HashSet<>());
        }
        likesStorage.get(filmId).add(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        if (!likesStorage.containsKey(filmId)) {
            likesStorage.put(filmId, new HashSet<>());
        }
        likesStorage.get(filmId).remove(userId);
    }
}
