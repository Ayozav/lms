package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.BusyOpenIDException;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.UserProducer;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class UsersController extends ControllerSkeleton {

    private final UsersEventRepository usersEventRepository;
    private final UserProducer userProducer;

    public UsersController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.usersEventRepository = new UsersEventRepository(factory);
        this.userProducer = new UserProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            String firstName = this.queryParam(ctx, "first_name");
            String lastName = this.queryParam(ctx, "last_name");
            String patronymic = this.queryParam(ctx, "patronymic");
            LocalDate birthDate = this.parseDate("birth_date", this.queryParam(ctx, "birth_date"));

            UUID openID = this.parseUIID(
                    "open_id",
                    this.queryParam(ctx, "open_id")
            );

            Optional<User> existing = this.usersEventRepository.getByOpenId(openID);
            if (existing.isPresent()) throw new BusyOpenIDException(openID);

            User user = new User(0, openID, firstName, lastName, patronymic, birthDate);
            new OkResponse(ctx);
            this.userProducer.produceAdd(openID.toString(), user);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }

    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.usersEventRepository.getPage(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Optional<User> user = this.usersEventRepository.getById(id);
            if (user.isEmpty()) throw new ObjectNotFoundInDatabase("Пользователь", id);
            new OkResponse(ctx);
            this.userProducer.produceDelete(user.get().getOpenID().toString(), user.get());
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Optional<User> user = this.usersEventRepository.getById(id);
            if (user.isEmpty()) throw new ObjectNotFoundInDatabase("Пользователь", id);
            new ObjectResponse<>(ctx, user.get());
        }
        catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }


    public void getByOpenId(Context ctx) {
        try {
            UUID openID = this.parseUIID("open_id", ctx.queryParam("open_id"));
            Optional<User> user = this.usersEventRepository.getByOpenId(openID);
            if (user.isEmpty()) throw new ObjectNotFoundInDatabase("Пользователь");
            new ObjectResponse<>(ctx, user.get());
        }
        catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            String firstName = this.queryParam(ctx, "first_name");
            String lastName = this.queryParam(ctx, "last_name");
            String patronymic = this.queryParam(ctx, "patronymic");
            LocalDate birthDate = this.parseDate("birth_date", this.queryParam(ctx, "birth_date"));
            UUID openID = this.parseUIID(
                    "open_id",
                    this.queryParam(ctx, "open_id")
            );

            Optional<User> existing = usersEventRepository.getById(id);
            if (existing.isEmpty()) throw new ObjectNotFoundInDatabase("Пользователь", id);

            User user = new User(id, openID, firstName, lastName, patronymic, birthDate);
            this.userProducer.produceUpdate(
                    user.getOpenID().toString(),
                    user
            );

            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}
