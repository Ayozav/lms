package ru.ayozav.javalin.exceptions;

public class DateRangeException extends GreatException {
    public DateRangeException() {
        super("'start' не может быть позже 'end'");
    }
}