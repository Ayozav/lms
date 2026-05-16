package ru.ayozav.controllers;

import io.javalin.http.Context;
import org.apache.commons.lang3.ObjectUtils;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.EchoAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UsersController {

    private final UsersEventRepository usersEventRepository;

    public UsersController(HikariConnectionFactory factory) {
        this.usersEventRepository = new UsersEventRepository(factory);
    }

    public void addUser(Context ctx) {
        String firstName = ctx.queryParam("first_name");
        String lastName = ctx.queryParam("last_name");
        String patronymic = ctx.queryParam("patronymic");
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(
                    Objects.requireNonNull(ctx.queryParam("birth_date")),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );
        }
        catch (DateTimeParseException | NumberFormatException | NullPointerException exc) {
            ctx.status(400).json(new BadArgumentsAnswer("'birth_date' должен быть в формате ДД.ММ.ГГГГ"));
            return;
        }

        UUID openID;
        try {
            openID = UUID.fromString(
                    Objects.requireNonNull(ctx.queryParam("open_id"))
            );
        }
        catch (IllegalArgumentException | NullPointerException exc) {
            ctx.status(400).json(new BadArgumentsAnswer("'open_id' неверный"));
            return;
        }
        try {
            int newUserID = this.usersEventRepository.addUser(
                    openID, firstName, lastName, patronymic, birthDate
            );

            ctx.status(200).json(new SuccessObjectInsertAnswer("user", newUserID));
        }
        catch (DatabaseException exc) {
            ctx.status(400).json(new BadArgumentsAnswer("Такой пользователь не может быть добавлен."));
        }
    }

    public void getUsers(Context ctx) {
        int page;
        try {
            page = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("page")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'page' должен быть целым положительным числом"));
            return;
        }

        if (page <= 0) {
            ctx.status(400).json(new BadArgumentsAnswer("'page' должен быть положительным."));
            return;
        }
        ctx.status(200).json(this.usersEventRepository.getUsers(page));
    }

    public void deleteUser(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }

        Optional<User> user = this.usersEventRepository.getUserById(id);
        if (user.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer(
                    "Нет пользователя к удалению с 'id' " + id
            ));
            return;
        }

        this.usersEventRepository.deleteUserById(id);
        ctx.status(200);
    }

    public void getUser(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }

        if (id <= 0) {
            ctx.status(404);
            return;
        }

        Optional<User> user = this.usersEventRepository.getUserById(id);

        if (user.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(user.get());
    }
}
