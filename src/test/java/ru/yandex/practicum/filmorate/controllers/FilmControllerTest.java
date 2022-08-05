package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {


    @Test
    void testNotValidDesc() {
        Film film = new Film(1, "name",
                "DescwfasdfsadfasdfasdfsadfasdfasdfsadfasdfasdfasdfDescwfasdfsadfasdfasdfsadfasdfasdfsadfasdfas" +
                        "dfasdfDescwfasdfsadfasdfasdfsadfasdfasdfsadfasdfasdfasdfDescwfasdfsadfasdfasdfsadfasdfasdfsadfasdfasdfasdf1",
                LocalDate.of(2020, 8, 3), 100);
        FilmController filmController = new FilmController();
        assertFalse(filmController.isValidFilm(film));
    }

    @Test
    void testNotValidDate() {
        Film film = new Film(1, "name",
                "Desc",
                LocalDate.of(1895, 12, 28), 100);
        Film film1 = new Film(1, "name",
                "Desc",
                LocalDate.of(1895, 12, 27), 100);
        FilmController filmController = new FilmController();
        assertFalse(filmController.isValidFilm(film1));
        assertTrue(filmController.isValidFilm(film));
    }

    @Test
    void testNotValidDuration() {
        Film film = new Film(1, "name",
                "Desc",
                LocalDate.of(1895, 12, 30), 1);
        Film film1 = new Film(1, "name",
                "Desc",
                LocalDate.of(1895, 12, 30), -1);
        FilmController filmController = new FilmController();
        assertFalse(filmController.isValidFilm(film1));
        assertTrue(filmController.isValidFilm(film));
    }
}

