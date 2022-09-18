package ru.yandex.practicum.filmorate.model.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    private Integer id;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
    private LocalDate birthday;
    //private final Set<Integer> friends = new HashSet<>();
}