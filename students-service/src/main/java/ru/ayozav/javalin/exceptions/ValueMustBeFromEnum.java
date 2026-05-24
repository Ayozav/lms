package ru.ayozav.javalin.exceptions;

import java.util.Arrays;

public class ValueMustBeFromEnum extends GreatException {
    public <E extends Enum<E>> ValueMustBeFromEnum (String paramName, String givenValue, Class<E> enumClass) {
        super("Параметр " + paramName + " (" + givenValue + ") " +
                "должен принимать значение из списка: " + enumValuesToString(enumClass)
        );
    }

    public static <E extends Enum<E>> String enumValuesToString(Class<E> enumClass) {
        return String.join(", ", Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new));
    }

}
