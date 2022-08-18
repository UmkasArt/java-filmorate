package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Пользователь: {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.debug("Пользователь: {}", user);
        return userService.put(user);
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable int userId) {
        return userService.getUsersMap().get(userId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userService.getUsersMap().get(id) == null  || userService.getUsersMap().get(friendId) == null) {
            throw new NoSuchElementException();
        }
        userService.getUsersMap().get(id).getFriendsSet().add(friendId);
        userService.getUsersMap().get(friendId).getFriendsSet().add(id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userService.getUsersMap().get(id) == null  || userService.getUsersMap().get(friendId) == null) {
            throw new NoSuchElementException();
        }
        userService.getUsersMap().get(id).getFriendsSet().remove(friendId);
        userService.getUsersMap().get(friendId).getFriendsSet().remove(id);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        if (userService.getUsersMap().get(id) == null) {
            throw new NoSuchElementException();
        }
        return userService.getUsersMap().get(id).getFriendsSet()
                .stream()
                .map(friendId -> userService.getUsersMap().get(friendId))
                .collect(Collectors.toList());
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        List<User> commonFriends = new ArrayList<>();
        for (Integer userId : userService.getUsersMap().get(id).getFriendsSet()) {
            for (Integer otherUserId : userService.getUsersMap().get(otherId).getFriendsSet()) {
                if (Objects.equals(userId, otherUserId)) {
                    commonFriends.add(userService.getUsersMap().get(userId));
                }
            }
        }
        return commonFriends;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidation(final ValidationException e) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoElement(final NoSuchElementException e/*todo найти и справить на подходящее исключение*/) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleServerError(final RuntimeException e/*todo найти и справить на подходящее исключение*/) {
    }

}
