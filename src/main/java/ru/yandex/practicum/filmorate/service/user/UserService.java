package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    private static final LocalDate TODAY_DATE = LocalDate.now();

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        log.debug("Текущее количество постов: {}", userStorage.getUsersStorage().size());
        return new ArrayList<>(userStorage.getUsersStorage().values());
    }

    public User create(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException();
        }
        validateUser(user);
        userStorage.add(user.getId(), user);
        return userStorage.getUserById(user.getId());
    }

    public User findUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(int id, int friendId) {
        if (userStorage.getUserById(id) != null | userStorage.getUserById(friendId) != null) {
            userStorage.addFriend(id, friendId);
        }
    }

    public List<User> getUserFriend(int id) {
        if (userStorage.getUserById(id) != null) {
            return userStorage.getUserFriend(id);
        }
        return new ArrayList<>();
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    public void deleteFriend(int id, int friendId) {
        if (userStorage.getUserById(id) != null || userStorage.getUserById(friendId) != null) {
            userStorage.deleteFriend(id, friendId);
        }
    }

    public User put(User user) {
        validateUser(user);
        userStorage.put(user.getId(), user);
        return userStorage.getUserById(user.getId());
    }

    public void validateUser(@NotNull User user) {
        if (user.getBirthday().isAfter(TODAY_DATE)) {
            throw new ValidationException("Invalid date of birth");
        }
    }
}
