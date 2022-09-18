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
import java.util.Map;
import java.util.NoSuchElementException;

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

    private Map<Integer, User> getUsersMap() {
        return userStorage.getUsersStorage();
    }

    public User create(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException();
        }
        validateUser(user);
        userStorage.add(user.getId(), user);
        return getUsersMap().get(user.getId());
    }

    public User findUserById(int id) {
        if (!getUsersMap().containsKey(id)) {
            throw new NoSuchElementException();
        }
        return getUsersMap().get(id);
    }

    public void addFriend(int id, int friendId) {
        if (getUsersMap().get(id) == null || getUsersMap().get(friendId) == null) {
            throw new NoSuchElementException();
        }
        userStorage.addFriend(id, friendId);
    }

    public List<User> getUserFriend(int id) {
        if (getUsersMap().get(id) == null) {
            throw new NoSuchElementException();
        }
        return userStorage.getUserFriend(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    public void deleteFriend(int id, int friendId) {
        if (getUsersMap().get(id) == null || getUsersMap().get(friendId) == null) {
            throw new NoSuchElementException();
        }
        userStorage.deleteFriend(id, friendId);
    }

    public User put(User user) {
        if (!userStorage.getUsersStorage().containsKey(user.getId())) {
            throw new NoSuchElementException();
        }
        validateUser(user);
        userStorage.put(user.getId(), user);
        return getUsersMap().get(user.getId());
    }

    public void validateUser(@NotNull User user) {
        if (user.getBirthday().isAfter(TODAY_DATE)) {
            throw new ValidationException("Invalid date of birth");
        }
    }
}
