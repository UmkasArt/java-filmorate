package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.genre.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;
    private final JdbcTemplate jdbcTemplate;
    protected Genre genre;

    @BeforeEach
    void setUp() {
        genre = new Genre(1, "Комедия");
    }

    @Test
    public void testFindGenreById() {
        Genre genreTest = genreDbStorage.getGenres().get(1);
        assertEquals(genre, genreTest);
    }
}
