package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    protected Film film;
    protected FilmService filmService;

    @BeforeEach
    void setUp() {
        film = new Film(1, "name",
                "Desc",
                LocalDate.of(2020, 8, 3), 100, null);
        filmService = new FilmService(new InMemoryFilmStorage());
    }

    @Test
    void testOk() {
        assertDoesNotThrow(() -> filmService.validateFilm(film));
    }

    @Test
    void testNotValidLengthDesc() {
        film.setDescription("f".repeat(201));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.validateFilm(film));
        assertEquals("Description can not be more than 200 characters", exception.getMessage());
    }

    @Test
    void testBoundaryLengthDesc() {
        film.setDescription("f".repeat(200));
        assertDoesNotThrow(() -> filmService.validateFilm(film));
    }

    @Test
    void testNotValidDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmService.validateFilm(film));
        assertEquals("The release date of the film before the birthday of the movie", exception.getMessage());
    }

    @Test
    void testBoundaryDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> filmService.validateFilm(film));
    }

    @Test
    void testNotValidDuration() {
        film.setDuration(0);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmService.validateFilm(film));
        assertEquals("Invalid duration", exception.getMessage());
    }

    @Test
    void testBoundaryDuration() {
        film.setDuration(1);
        assertDoesNotThrow(() -> filmService.validateFilm(film));
    }
}