package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.util.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static int generatorId = 0;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Film> getFilmsStorage() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT FILMS.*, MPA.NAME AS MPA_NAME, c.CATEGORY_ID as GENRE_ID, c.NAME AS GENRE_NAME " +
                "FROM FILMS left join MPA on FILMS.MPA_ID = MPA.MPA_ID inner join FILM_CATEGORY FC on FILMS.ID = FC.FILM_ID " +
                "inner join CATEGORY C on C.CATEGORY_ID = FC.CATEGORY_ID");
        Map<Integer, Film> mapFilms = new HashMap<>();
        while (filmRows.next()) {
            if (mapFilms.containsKey(filmRows.getInt("id"))) {
                mapFilms.get(filmRows.getInt("id"))
                        .getGenres()
                        .add(new Genre(filmRows.getInt("GENRE_ID"), filmRows.getString("GENRE_NAME")));
                continue;
            }
            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getString("description"),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate"),
                    new Mpa(filmRows.getInt("mpa_id"), filmRows.getString("MPA_NAME").trim()));
            film.getGenres().add(new Genre(filmRows.getInt("GENRE_ID"), filmRows.getString("GENRE_NAME")));
            mapFilms.put(film.getId(), film);
        }
        return mapFilms;
    }

    @Override
    public Film getFilmById(Integer id) {
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT FILMS.*, MPA.NAME AS MPA_NAME " +
                "FROM FILMS left join MPA on FILMS.MPA_ID = MPA.MPA_ID where FILMS.ID = ?", id);
        if (!filmRow.next()) {
            throw new NoSuchElementException();
        }
        Film film = new Film(
                filmRow.getInt("id"),
                filmRow.getString("name"),
                filmRow.getDate("release_date").toLocalDate(),
                filmRow.getString("description"),
                filmRow.getInt("duration"),
                filmRow.getInt("rate"),
                new Mpa(filmRow.getInt("mpa_id"), filmRow.getString("MPA_NAME").trim()));
        String sql = "SELECT cat.category_id AS ID, cat.name AS NAME " +
                "FROM category AS cat INNER JOIN film_category fc " +
                "ON cat.category_id = fc.category_id " +
                "WHERE fc.film_id = ?";
        List<Genre> genreList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class), film.getId());
        film.getGenres().addAll(genreList);
        return film;
    }

    @Override
    public void addFilm(Integer id, Film film) {
        film.setId(++generatorId);
        jdbcTemplate.update("insert into films(id, name, description, release_date, duration, rate, mpa_id) values (?, ?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa() == null ? null : film.getMpa().getId());
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT name FROM MPA where MPA_ID = ?", film.getMpa().getId());
        if (mpaRows.next()) {
            film.getMpa().setName(mpaRows.getString("name").trim());
        }
        updateFilmsGenreName(film);
    }

    @Override
    public void putFilm(Integer id, Film film) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", film.getId());
        if (!filmRows.next()) {
            throw new NoSuchElementException("Фильм не найден");
        }
        jdbcTemplate.update("update films set name = ?, description = ?, release_date = ?, duration = ?, " +
                        "rate = ?, mpa_id = ? where id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                film.getId());
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA where MPA_ID = ?", film.getMpa().getId());
        if (mpaRows.next()) {
            film.getMpa().setName(mpaRows.getString("name").trim());
        }
        jdbcTemplate.update("delete from film_category where film_id = ?", film.getId());

        updateFilmsGenreName(film);
    }

    @Override
    public void removeFilm(Integer id) {
        jdbcTemplate.update("delete from films where id = ?",
                id);
        jdbcTemplate.update("delete from film_category where film_id = ?",
                id);
    }

    @Override
    public List<Film> getOrderByLikeFilms(Integer count) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select id, FILMS.name as FILM_NAME, description, release_date, " +
                        "duration, FILMS.MPA_ID, RATE, M.NAME AS mpa_name, count(USER_ID) + RATE as CNT from FILMS " +
                        "left join LIKES L on FILMS.ID = L.FILM_ID inner join MPA M on FILMS.MPA_ID = M.MPA_ID " +
                        "group by id, FILMS.name, description, release_date, duration, FILMS.MPA_ID, RATE order by CNT desc limit ?",
                count);
        List<Film> listFilms = new ArrayList<>();
        while (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("FILM_NAME"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getString("description"),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate"),
                    new Mpa(filmRows.getInt("MPA_ID"), filmRows.getString("MPA_NAME").trim()));
            String sql = "SELECT cat.category_id AS id, cat.name AS name " +
                    "FROM category AS cat INNER JOIN film_category fc " +
                    "ON cat.category_id = fc.category_id " +
                    "WHERE fc.film_id = ?";
            List<Genre> genreList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class), film.getId());
            film.getGenres().addAll(genreList);
            listFilms.add(film);
        }
        return listFilms;
    }

    @Override
    public void putLike(Integer filmId, Integer userId) {
        checkIdsForLikes(filmId, userId);
        jdbcTemplate.update("insert into likes(film_id, user_id) values (?, ?)",
                filmId,
                userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        checkIdsForLikes(filmId, userId);
        jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?",
                filmId,
                userId);
    }

    private void updateFilmsGenreName(Film film) {
        List<Object[]> objectGenreList = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            objectGenreList.add(new Object[]{film.getId(), genre.getId()});
        }

        jdbcTemplate.batchUpdate("insert into FILM_CATEGORY(film_id, category_id) values (?, ?)", objectGenreList);

        String sql = "select c.CATEGORY_ID as GENRE_ID, C.NAME AS GENRE_NAME from FILM_CATEGORY " +
                "inner join CATEGORY C on C.CATEGORY_ID = FILM_CATEGORY.CATEGORY_ID " +
                "WHERE FILM_CATEGORY.FILM_ID = ? ORDER BY GENRE_ID";

        film.getGenres().addAll(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class), film.getId()));
    }

    private void checkIdsForLikes(Integer filmId, Integer userId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", filmId);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", userId);
        if (!filmRows.next()) {
            throw new ValidationException("Фильм не найден");
        }
        if (!userRows.next()) {
            throw new ValidationException("Пользователь не найден");
        }
    }
}
