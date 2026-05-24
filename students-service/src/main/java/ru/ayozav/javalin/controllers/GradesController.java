package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.GradesEventRepository;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.GradeProducer;
import ru.ayozav.models.Grade;
import ru.ayozav.models.GradeType;

import java.util.Optional;

public class GradesController extends ControllerSkeleton {
    private final GradesEventRepository gradesEventRepository;
    private final UsersEventRepository usersEventRepository;
    private final GradeProducer producer;

    public GradesController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.gradesEventRepository = new GradesEventRepository(factory);
        this.usersEventRepository = new UsersEventRepository(factory);
        this.producer = new GradeProducer(kafka_bootstrap_server);

    }

    public void add(Context ctx) {
        try {
            String code = this.queryParam(ctx, "code");
            String gradeName = this.queryParam(ctx, "grade_name");
            String gradeType = this.parseFromEnum(
                    "grade_type",
                    this.queryParam(ctx, "grade_type"),
                    GradeType.class
            ).getCode();

            int supervisorID = this.parsePositiveInt(ctx, "supervisor_id");
            this.getUser(supervisorID, this.usersEventRepository);
            Grade grade = new Grade(0, code, gradeName, supervisorID, gradeType);
            this.producer.produceAdd(code, grade);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.gradesEventRepository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Optional<Grade> grade = this.gradesEventRepository.getById(id);
            if (grade.isEmpty()) throw new ObjectNotFoundInDatabase("Уровень подготовки", id);
            new ObjectResponse<>(ctx, grade.get());
        }
        catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Optional<Grade> grade = this.gradesEventRepository.getById(id);
            if (grade.isEmpty()) throw new ObjectNotFoundInDatabase("Уровень подготовки", id);

            new OkResponse(ctx);
            this.producer.produceDelete(grade.get().getCode(), grade.get());
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }


    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            String code = this.queryParam(ctx, "code");
            String gradeName = this.queryParam(ctx, "grade_name");
            String gradeType = this.parseFromEnum(
                    "grade_type",
                    this.queryParam(ctx, "grade_type"),
                    GradeType.class
            ).getCode();

            int supervisorID = this.parsePositiveInt(ctx, "supervisor_id");
            Optional<Grade> existing = this.gradesEventRepository.getById(id);
            if (existing.isEmpty()) throw new ObjectNotFoundInDatabase("Уровень подготовки", id);

            Grade grade = new Grade(id, code, gradeName, supervisorID, gradeType);
            this.producer.produceUpdate(grade.getCode(), grade);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}
