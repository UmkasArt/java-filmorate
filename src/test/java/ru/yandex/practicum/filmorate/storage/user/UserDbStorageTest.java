package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
    protected User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "ololol@ya.ru", "login", "name", LocalDate.of(2000, 1, 1));
        jdbcTemplate.update("delete from FRIENDS");
        jdbcTemplate.update("delete from USERS");
    }

    @Test
    public void testFindUserById() {
        jdbcTemplate.update("insert into USERS(id, login, name, email, birthday) " +
                "values (1, 'ololol@ya.ru', 'login', 'name', '2000-01-01')");
        User userTest = userStorage.getUserById(1);
        assertEquals(user, userTest);
    }

    @Test
    public void testAddUser() {
        userStorage.add(user.getId(), user);
        User userTest = userStorage.getUserById(1);
        assertEquals(user, userTest);
    }

    @Test
    public void testPutUser() {
        userStorage.add(user.getId(), user);
        user.setName("loginTest");
        userStorage.put(user.getId(), user);
        User userTest = userStorage.getUserById(2);
        assertEquals(user, userTest);
    }

    @Test
    public void testRemoveUser() {
        userStorage.add(user.getId(), user);
        userStorage.remove(user.getId());

        final NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> userStorage.put(user.getId(), user));
    }

    @Test
    public void testAddFriend() {
        userStorage.add(user.getId(), user);
        User user2 = new User(2, "ololol@ya.ru", "login2", "name2",
                LocalDate.of(2000, 1, 1));
        userStorage.add(user2.getId(), user2);

        userStorage.addFriend(user.getId(), user2.getId());

        assertEquals(List.of(user2), userStorage.getUserFriend(user.getId()));
    }

    @Test
    public void testDeleteFriend() {
        userStorage.add(user.getId(), user);
        User user2 = new User(2, "ololol@ya.ru", "login2", "name2",
                LocalDate.of(2000, 1, 1));
        userStorage.add(user2.getId(), user2);
        userStorage.addFriend(user.getId(), user2.getId());
        userStorage.deleteFriend(user.getId(), user2.getId());
        assertEquals(0, userStorage.getUserFriend(user.getId()).size());
    }
}
