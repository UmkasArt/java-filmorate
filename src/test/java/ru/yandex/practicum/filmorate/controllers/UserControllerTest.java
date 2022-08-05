package ru.yandex.practicum.filmorate.controllers;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;


class UserControllerTest {

    @Test
    void testNotValidBirthday(){
        User user = new User(1, "ololol@ya.ru", "", "name", LocalDate.of(2022, 8, 6));
        UserController userController = new UserController();
        assertFalse(userController.isValidUser(user));
    }
}

