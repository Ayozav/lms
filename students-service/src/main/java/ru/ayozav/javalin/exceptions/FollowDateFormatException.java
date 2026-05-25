package ru.ayozav.javalin.exceptions;

public class FollowDateFormatException extends GreatException {
    public FollowDateFormatException(String format, String paramName) {
        super("Дата " + paramName + " должна быть в формате " + format);
    }
}
