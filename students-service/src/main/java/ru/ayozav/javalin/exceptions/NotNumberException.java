package ru.ayozav.javalin.exceptions;

public class NotNumberException extends GreatException {
    public NotNumberException(String paramName) {
        super("Параметр " + paramName + " должен быть целым числом.");
    }
}
