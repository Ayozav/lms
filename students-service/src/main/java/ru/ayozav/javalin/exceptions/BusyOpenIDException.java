package ru.ayozav.javalin.exceptions;

import java.util.UUID;

public class BusyOpenIDException extends GreatException {
    public BusyOpenIDException(UUID openID) {
        super("Указанный open_id (" + openID + ") уже занят");
    }
}
