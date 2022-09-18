package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsersStorage();

    void add(Integer id, User user);

    void put(Integer id, User user);

    void remove(Integer id);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<User> getUserFriend(int id);

    List<User> getCommonFriends(int id, int otherId);
}
