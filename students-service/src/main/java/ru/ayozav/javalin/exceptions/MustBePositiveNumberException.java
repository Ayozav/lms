package ru.ayozav.javalin.exceptions;

public class MustBePositiveNumberException extends GreatException {
    public MustBePositiveNumberException(String paramName) {
        super("Параметр " + paramName + " должен быть положительным числом.");
    }
}
