package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private static int generatorId = 0;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Map<Integer, User> getUsersStorage() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users");
        Map<Integer, User> mapUsers = new HashMap<>();
        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getString("email"),
                    userRows.getDate("birthday").toLocalDate());
            mapUsers.put(user.getId(), user);
        }
        return mapUsers;
    }

    @Override
    public void add(Integer id, User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++generatorId);
        jdbcTemplate.update("insert into users(id, login, name, email, birthday) values (?, ?, ?, ?, ?)",
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday());
    }

    @Override
    public void put(Integer id, User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", user.getId());
        if (!userRows.next()) {
            throw new ValidationException("Пользователь не найден");
        }
        jdbcTemplate.update("update users set login = ?, name = ?, email = ?, birthday = ? where id = ?",
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public void remove(Integer id) {
        jdbcTemplate.update("delete from users where id = ?",
                id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        jdbcTemplate.update("insert into friends(user_id, friend_id, status) values (?, ?, ?)",
                id,
                friendId,
                false);
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friends where user_id = ? and friend_id = ?"
                , friendId, id);
        if (friendRows.next()) {
            jdbcTemplate.update("update friends set status = 'true' where user_id = ? and friend_id = ?",
                    id, friendId);
            jdbcTemplate.update("update friends set status = 'true' where user_id = ? and friend_id = ?",
                    friendId, id);
        }
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        jdbcTemplate.update("delete from friends where user_id = ? and friend_id = ?",
                id,
                friendId);
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friends where user_id = ? and friend_id = ?"
                , friendId, id);
        if (friendRows.next()) {
            jdbcTemplate.update("update friends set status = 'false' where user_id = ? and friend_id = ?",
                    friendId, id);
        }
    }

    @Override
    public List<User> getUserFriend(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select U.* from FRIENDS inner join USERS U on U.ID = FRIENDS.FRIEND_ID where FRIENDS.USER_ID = ?",
                id);
        List<User> listUsers = new ArrayList<>();
        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getString("email"),
                    userRows.getDate("birthday").toLocalDate());
            listUsers.add(user);
        }
        return listUsers;
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select U.* from FRIENDS f1 inner join FRIENDS f2 on f1.FRIEND_ID = f2.FRIEND_ID " +
                        "inner join USERS U on f1.FRIEND_ID = U.ID " +
                        "where f1.USER_ID = ? and f2.USER_ID = ?",
                id,
                otherId);
        List<User> listCommonFriend = new ArrayList<>();
        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getString("email"),
                    userRows.getDate("birthday").toLocalDate());
            listCommonFriend.add(user);
        }
        return listCommonFriend;
    }
}
