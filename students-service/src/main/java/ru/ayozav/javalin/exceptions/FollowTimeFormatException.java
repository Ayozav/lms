package ru.ayozav.javalin.exceptions;

public class FollowTimeFormatException extends GreatException {
    public FollowTimeFormatException(String paramName, String format) {
        super("Параметр " + paramName + " должен быть написан в формате " + format);
    }
}
