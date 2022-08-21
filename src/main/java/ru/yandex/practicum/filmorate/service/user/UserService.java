package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    private Map<Integer, User> getUsersMap() {
        return inMemoryUserStorage.getUsers();
    }

    public User create(User user) {
        if (user.getId() != 0) {
            throw new IllegalArgumentException();
        }
        validateUser(user);
        inMemoryUserStorage.add(user.getId(), user);
        return user;
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
        getUsersMap().get(id).getFriends().add(friendId);
        getUsersMap().get(friendId).getFriends().add(id);
    }

    public List<User> getUserFriend(int id) {
        if (getUsersMap().get(id) == null) {
            throw new NoSuchElementException();
        }
        return getUsersMap().get(id).getFriends()
                .stream()
                .map(friendId -> getUsersMap().get(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        for (Integer userId : getUsersMap().get(id).getFriends()) {
            for (Integer otherUserId : getUsersMap().get(otherId).getFriends()) {
                if (Objects.equals(userId, otherUserId)) {
                    commonFriends.add(getUsersMap().get(userId));
                }
            }
        }
        return commonFriends;
    }

    public void deleteFriend(int id, int friendId) {
        if (getUsersMap().get(id) == null || getUsersMap().get(friendId) == null) {
            throw new NoSuchElementException();
        }
        getUsersMap().get(id).getFriends().remove(friendId);
        getUsersMap().get(friendId).getFriends().remove(id);
    }

    public User put(User user) {
        if (!inMemoryUserStorage.getUsers().containsKey(user.getId())) {
            throw new NoSuchElementException();
        }
        validateUser(user);
        inMemoryUserStorage.put(user.getId(), user);
        return user;
    }

    public void validateUser(@NotNull User user) {
        if (user.getBirthday().isAfter(TODAY_DATE)) {
            throw new ValidationException("Invalid date of birth");
        }
    }
}
