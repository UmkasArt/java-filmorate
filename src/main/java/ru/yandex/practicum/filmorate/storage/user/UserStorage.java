package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    void put(int id, User user);

    void add(int id, User user);

    void remove(int id);
}
