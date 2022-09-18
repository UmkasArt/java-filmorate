package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;
    private final JdbcTemplate jdbcTemplate;
    protected Mpa mpa;

    @BeforeEach
    void setUp() {
        mpa = new Mpa(1, "G");
    }

    @Test
    public void testFindMpaById() {
        Mpa mpaTest = mpaDbStorage.getMpas().get(1);
        assertEquals(mpa, mpaTest);
    }
}
