package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    protected Film film;
    protected FilmController filmController;

    @BeforeEach
    void createVarsForTests() {
        film = new Film(1, "name",
                "Desc",
                LocalDate.of(2020, 8, 3), 100);
        filmController = new FilmController();
    }

    @Test
    void testOk() {
        assertDoesNotThrow(() -> filmController.isValidFilm(film));
    }
    @Test
    void testNotValidDesc() {
        film.setDescription("f".repeat(201));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.isValidFilm(film))
        ;
        assertEquals("Длина описания больше 200 символов", exception.getMessage());
    }

    @Test
    void testValidDesc() {
        film.setDescription("f".repeat(200));
        assertDoesNotThrow(() -> filmController.isValidFilm(film));
    }

    @Test
    void testNotValidDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.isValidFilm(film))
                ;
        assertEquals("Дата выхода фильма раньше дня рождения кино", exception.getMessage());
    }

    @Test
    void testValidDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> filmController.isValidFilm(film));
    }

    @Test
    void testNotValidDuration() {
        film.setDuration(0);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.isValidFilm(film))
                ;
        assertEquals("Невалидная длительность", exception.getMessage());
    }

    @Test
    void testValidDuration() {
        film.setDuration(1);
        assertDoesNotThrow(() -> filmController.isValidFilm(film));
    }
}

