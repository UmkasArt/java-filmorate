package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserService {
    private final UserStorage inMemoryUserStorage;

    private static final LocalDate TODAY_DATE = LocalDate.now();

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> getAllUsers() {
        log.debug("Текущее количество постов: {}", inMemoryUserStorage.getUsers().size());
        return new ArrayList<>(inMemoryUserStorage.getUsers().values());
    }

    public Map<Integer, User> getUsersMap() {
        return inMemoryUserStorage.getUsers();
    }

    public User create(User user) {
        isValidUser(user);
        inMemoryUserStorage.add(user.getId(), user);
        return user;
    }

    public User put(User user) {
        if (!inMemoryUserStorage.getUsers().containsKey(user.getId())) {
            throw new NoSuchElementException();
        }
        isValidUser(user);
        inMemoryUserStorage.put(user.getId(), user);
        return user;
    }

    public void isValidUser(User user) {
        if (user.getBirthday().isAfter(TODAY_DATE)) {
            throw new ValidationException("Невалидная дата рождения");
        }
    }
}
