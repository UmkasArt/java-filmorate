package ru.yandex.practicum.filmorate.model;

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
}