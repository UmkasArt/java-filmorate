package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.util.HashMap;
import java.util.Map;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Integer, Mpa> getMpas() {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from mpa");
        Map<Integer, Mpa> mapMpas = new HashMap<>();
        while (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("name").trim());
            mapMpas.put(mpa.getId(), mpa);
        }
        return mapMpas;
    }
}
