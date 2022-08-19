package ru.yandex.practicum.filmorate.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {

    protected User user;
    protected UserService userService;

    @BeforeEach
    void setUp() {
        user = new User(1, "ololol@ya.ru", "login", "name", LocalDate.of(2000, 1, 1), null);
        userService = new UserService(new InMemoryUserStorage());
    }


    @Test
    void testOk() {
        assertDoesNotThrow(() -> userService.isValidUser(user));
    }

    @Test
    void testNotValidBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.isValidUser(user));
        assertEquals("Невалидная дата рождения", exception.getMessage());
    }

    @Test
    void testBoundaryBirthday() {
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> userService.isValidUser(user));
    }
}