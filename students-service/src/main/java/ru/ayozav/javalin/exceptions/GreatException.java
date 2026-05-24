package ru.ayozav.javalin.exceptions;

public class GreatException extends RuntimeException {
    public GreatException(String message) {
        super(message);
    }

    public int getCode() {
        return 400;
    }
}
