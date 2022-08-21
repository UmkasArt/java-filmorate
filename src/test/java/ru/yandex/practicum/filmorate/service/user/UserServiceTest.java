package ru.yandex.practicum.filmorate.service.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserServiceTest {

    protected User user;
    protected UserService userService;

    @BeforeEach
    void setUp() {
        user = new User(1, "ololol@ya.ru", "login", "name", LocalDate.of(2000, 1, 1));
        userService = new UserService(new InMemoryUserStorage());
    }


    @Test
    void testOk() {
        assertDoesNotThrow(() -> userService.validateUser(user));
    }

    @Test
    void testNotValidBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.validateUser(user));
        assertEquals("Invalid date of birth", exception.getMessage());
    }

    @Test
    void testBoundaryBirthday() {
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> userService.validateUser(user));
    }
}