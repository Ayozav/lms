package ru.ayozav.javalin.exceptions;

public class FollowDateFormatException extends GreatException {
    public FollowDateFormatException(String format) {
        super("Дата должна быть в формате " + format);
    }
}
