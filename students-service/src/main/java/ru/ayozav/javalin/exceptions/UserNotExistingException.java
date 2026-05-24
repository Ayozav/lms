package ru.ayozav.javalin.exceptions;

public class UserNotExistingException extends GreatException {
    public UserNotExistingException(int userID) {
        super("Пользователя (id=" + userID + ") не существует.");
    }
}
