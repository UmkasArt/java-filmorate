package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;
    protected Film film;

    @BeforeEach
    void setUp() {
        film = new Film(1, "name",
                LocalDate.of(2020, 8, 3), "Desc", 100, 0, new Mpa(1, "G"));
        jdbcTemplate.update("delete from LIKES");
        jdbcTemplate.update("delete from FILMS");
    }

    @Test
    public void testFindFilmById() {
        jdbcTemplate.update("insert into FILMS(id, name, description, release_date, duration, rate, mpa_id) " +
                "values (1, 'name', 'Desc', '2020-08-03', 100, 0, 1)");
        Film filmTest = filmStorage.getFilmsStorage().get(1);
        assertEquals(film, filmTest);
    }

    @Test
    public void testAddFilm() {
        filmStorage.addFilm(film.getId(), film);
        Film filmTest = filmStorage.getFilmsStorage().get(1);
        assertEquals(film, filmTest);
    }

    @Test
    public void testPutFilm() {
        filmStorage.addFilm(film.getId(), film);
        film.setName("loginTest");
        filmStorage.putFilm(film.getId(), film);
        Film filmTest = filmStorage.getFilmsStorage().get(3);
        assertEquals(film, filmTest);
    }

    @Test
    public void testRemoveFilm() {
        filmStorage.addFilm(film.getId(), film);
        filmStorage.removeFilm(film.getId());

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmStorage.putFilm(film.getId(), film));
        assertEquals("Фильм не найден", exception.getMessage());
    }

    @Test
    public void testAddLike() {
        jdbcTemplate.update("insert into USERS(id, login, name, email, birthday) " +
                "values (1, 'ololol@ya.ru', 'login', 'name', '2000-01-01')");
        filmStorage.addFilm(film.getId(), film);
        filmStorage.putLike(2, 1);
        assertEquals(List.of(film), filmStorage.getOrderByLikeFilms(10));
    }

    @Test
    public void testDeleteLike() {
        jdbcTemplate.update("insert into USERS(id, login, name, email, birthday) " +
                "values (2, 'ololol@ya.ru', 'login', 'name', '2000-01-01')");
        filmStorage.addFilm(film.getId(), film);
        filmStorage.putLike(5, 2);
        assertEquals(List.of(film), filmStorage.getOrderByLikeFilms(10));
        filmStorage.deleteLike(5, 2);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select count(*) as total from LIKES where FILM_ID = ?", film.getId());
        if (sqlRowSet.next()) {
            assertEquals(0, sqlRowSet.getInt("total"));
        }
    }
}
