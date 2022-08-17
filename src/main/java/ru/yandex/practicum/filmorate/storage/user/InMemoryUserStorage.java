package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap();
    private static int generator = 0;

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public void add(int id, User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(++generator);
        }
        users.put(user.getId(), user);
    }

    @Override
    public void put(int id, User user) {
        if (users.get(user.getId()) == null) {
            throw new ValidationException("Пользователь не найден");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void remove(int id) {
        users.remove(id);
    }
}
