package ru.yandex.practicum.filmorate.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {

    protected User user;
    protected UserController userController;

    @BeforeEach
    void createVarsForTests() {
        user = new User(1, "ololol@ya.ru", "login", "name", LocalDate.of(2022, 8, 6));
        userController = new UserController();
    }


    @Test
    void testOk() {
        assertDoesNotThrow(() -> userController.isValidUser(user));
    }

    @Test
     void testNotValidBirthday(){
        user.setBirthday(LocalDate.now().plusDays(1));
         final ValidationException exception = assertThrows(
                 ValidationException.class,
                 () -> userController.isValidUser(user))
                 ;
         assertEquals("Невалидная дата рождения", exception.getMessage());
    }

    @Test
    void testValidBirthday(){
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> userController.isValidUser(user));
    }
}

