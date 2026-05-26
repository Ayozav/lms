package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.*;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс-скелет для уже готовых функций многократного использования
 * Его причина существования: желание устранить дубляжи
 * */
public class ControllerSkeleton {

    public int parsePositiveInt(Context ctx, String paramName) throws MustBePositiveNumberException {
        try {
            int value = Integer.parseInt(Objects.requireNonNull(ctx.queryParam(paramName)));
            if (value <= 0) throw new MustBePositiveNumberException(paramName);
            return value;
        } catch (NumberFormatException | NullPointerException e) {
            throw new NotNumberException(paramName);
        }
    }

    public String queryParam(Context ctx, String param) throws ParamMustBeException {
        try {
            return Objects.requireNonNull(ctx.queryParam(param));
        }
        catch (NullPointerException e) {
            throw new ParamMustBeException(param);
        }
    }

    public LocalDate parseDate(String paramName, String paramValue) throws FollowDateFormatException{
        LocalDate dateParam;
        try {
            dateParam = LocalDate.parse(
                    Objects.requireNonNull(paramValue),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );
            return dateParam;
        }
        catch (DateTimeParseException | NumberFormatException | NullPointerException exc) {
            throw new FollowDateFormatException("ДД.ММ.ГГГГ", paramName);
        }
    }

    public LocalTime parseTime(Context ctx, String paramName) throws FollowTimeFormatException, ParamMustBeException {
        try {
            String timeStr = this.queryParam(ctx, paramName);
            return LocalTime.parse(timeStr);
        } catch (DateTimeParseException e) {
            throw new FollowTimeFormatException(paramName, "HH:MM[:SS]");
        }
    }

    public UUID parseUIID(String paramName, String paramValue) throws BadUUIDException, ParamMustBeException {
        try {
            return UUID.fromString(
                    Objects.requireNonNull(paramValue)
            );
        } catch (IllegalArgumentException e) {
            throw new BadUUIDException(paramValue);
        }
        catch (NullPointerException e) {
            throw new ParamMustBeException(paramName);
        }
    }

    public <E extends Enum<E>> E parseFromEnum(String paramName, String value, Class<E> enumClass) {
        try {
            return Optional.of(Enum.valueOf(enumClass, value)).get();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ValueMustBeFromEnum(paramName, value, enumClass);
        }
    }

    public User getUser(int userID, UsersEventRepository repository) {
        Optional<User> user = repository.getById(userID);
        if (user.isEmpty()) throw new UserNotExistingException(userID);
        return user.get();
    }
}
