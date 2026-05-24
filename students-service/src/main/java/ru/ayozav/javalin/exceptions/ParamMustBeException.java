package ru.ayozav.javalin.exceptions;

public class ParamMustBeException extends GreatException {
    public ParamMustBeException(String paramName) {
        super("Параметр " + paramName + " должен быть передан.");
    }
}
