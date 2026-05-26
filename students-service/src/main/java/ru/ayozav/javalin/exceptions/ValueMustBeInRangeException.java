package ru.ayozav.javalin.exceptions;

public class ValueMustBeInRangeException extends GreatException {
    public ValueMustBeInRangeException(String paramName, int fromVal, int toVal) {
        super("Число для параметра " + paramName + " должно быть в пределах от " + fromVal + " до " + toVal);
    }
}
