package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap();
    private static final LocalDate TODAY_DATE= LocalDate.now();
    private static int generator = 0;

    @GetMapping
    public java.util.Collection<User> findAll() {
        log.debug("Текущее количество постов: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Пользователь: {}", user);
        isValidUser(user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(++generator);
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.debug("Пользователь: {}", user);
        isValidUser(user);
        if(users.get(user.getId()) == null) {
            throw new ValidationException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    void isValidUser(User user) {
        if (user.getBirthday().isAfter(TODAY_DATE)) {
            throw new ValidationException("Невалидная дата рождения");
        }
    }


}
