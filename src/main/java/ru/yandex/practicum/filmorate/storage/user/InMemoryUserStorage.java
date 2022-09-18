package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;

@Component("inMemoryStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> usersStorage = new HashMap();
    private final Map<Integer, Set<User>> friendshipStorage = new HashMap<>();
    private static int generatorId = 0;

    @Override
    public Map<Integer, User> getUsersStorage() {
        return usersStorage;
    }

    @Override
    public void add(Integer id, User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++generatorId);
        usersStorage.put(user.getId(), user);
    }

    @Override
    public void put(Integer id, User user) {
        if (usersStorage.get(user.getId()) == null) {
            throw new ValidationException("Пользователь не найден");
        }
        usersStorage.put(user.getId(), user);
    }

    @Override
    public void remove(Integer id) {
        usersStorage.remove(id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        if (!friendshipStorage.containsKey(id)) {
            friendshipStorage.put(id, new HashSet<>());
        }
        friendshipStorage.get(id).add(usersStorage.get(friendId));
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        if (!friendshipStorage.containsKey(id)) {
            friendshipStorage.put(id, new HashSet<>());
        }
        friendshipStorage.get(id).remove(usersStorage.get(friendId));
    }

    @Override
    public List<User> getUserFriend(int id) {
        return new ArrayList<>(friendshipStorage.get(id));
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        for (User user : friendshipStorage.get(id)) {
            for (User otherUser : friendshipStorage.get(otherId)) {
                if (Objects.equals(user, otherUser)) {
                    commonFriends.add(user);
                }
            }
        }
        return commonFriends;
    }
}
