package ru.yandex.practicum.filmorate.model.genre;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Genre {
    private Integer id;
    private String name;

    public Genre() {
    }
}
