package ru.ayozav.javalin.exceptions;

public class ObjectNotFoundInDatabase extends GreatException {
    public ObjectNotFoundInDatabase(String object, int id) {
        super(object + " (id=" + id + ") не был найден.");
    }

    @Override
    public int getCode() {
        return 404;
    }
}
