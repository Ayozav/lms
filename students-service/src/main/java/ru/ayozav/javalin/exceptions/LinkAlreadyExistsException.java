package ru.ayozav.javalin.exceptions;

public class LinkAlreadyExistsException extends GreatException {
    public LinkAlreadyExistsException(String linkName) {
        super(linkName + " уже существует.");
    }
}
