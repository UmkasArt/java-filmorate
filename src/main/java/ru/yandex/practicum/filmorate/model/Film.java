package ru.yandex.practicum.filmorate.model;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Film {
    int id;
    @NotNull
    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    Set<Integer> likes;

    public Set<Integer> getLikesSet() {
        if (this.likes == null) {
            this.likes = new HashSet<>();
        }
        return this.likes;
    }
}
