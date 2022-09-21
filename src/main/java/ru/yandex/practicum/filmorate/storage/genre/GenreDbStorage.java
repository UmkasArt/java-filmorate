package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.HashMap;
import java.util.Map;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Integer, Genre> getGenres() {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from category");
        Map<Integer, Genre> mapGenres = new HashMap<>();
        while (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("category_id"),
                    genreRows.getString("name"));
            mapGenres.put(genre.getId(), genre);
        }
        return mapGenres;
    }
}
