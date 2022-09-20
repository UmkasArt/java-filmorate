package ru.yandex.practicum.filmorate.model.film;


import lombok.*;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotNull
    @NotBlank
    private String name;
    private LocalDate releaseDate;
    private String description;
    private Integer duration;
    private int rate;
    private final Mpa mpa;
    private final Set<Genre> genres = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
}
