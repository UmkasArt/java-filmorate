package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();

    void add(int id, User user);

    void put(int id, User user);

    void remove(int id);
}
