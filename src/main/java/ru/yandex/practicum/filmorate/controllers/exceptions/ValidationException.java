package ru.yandex.practicum.filmorate.controllers.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
