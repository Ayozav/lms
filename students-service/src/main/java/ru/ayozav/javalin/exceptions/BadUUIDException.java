package ru.ayozav.javalin.exceptions;

public class BadUUIDException extends GreatException {
    public BadUUIDException(String openID) {
        super("Указанный openID ('" + openID + "') не является UUID.");
    }
}
