package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GenreService {

    private final GenreDbStorage genreStorage;


    @Autowired
    public GenreService(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAllGenres() {
        return new ArrayList<>(genreStorage.getGenres().values());
    }

    public Genre findGenreById(Integer genreId) {
        if (!genreStorage.getGenres().containsKey(genreId)) {
            throw new NoSuchElementException();
        }
        return genreStorage.getGenres().get(genreId);
    }
}
