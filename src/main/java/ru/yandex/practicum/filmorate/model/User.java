package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
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
public class User {
    int id;
    @NotNull
    @NotBlank
    String login;
    String name;
    @NotNull
    @NotBlank
    @Email
    String email;
    LocalDate birthday;
    Set<Integer> friends;

    public Set<Integer> getFriendsSet() {
        if (this.friends == null) {
            this.friends = new HashSet<>();
        }
        return this.friends;
    }
}