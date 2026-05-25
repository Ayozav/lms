package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.DisciplinesEventRepository;
import ru.ayozav.database.repositories.GradesEventRepository;
import ru.ayozav.database.repositories.SemestersEventRepository;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.DisciplineProducer;
import ru.ayozav.models.Discipline;

public class DisciplinesController extends ControllerSkeleton {

    private final DisciplinesEventRepository disciplinesEventRepository;
    private final SemestersEventRepository semestersEventRepository;
    private final GradesEventRepository gradesEventRepository;
    private final UsersEventRepository usersEventRepository;
    private final DisciplineProducer producer;

    public DisciplinesController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.disciplinesEventRepository = new DisciplinesEventRepository(factory);
        this.semestersEventRepository = new SemestersEventRepository(factory);
        this.gradesEventRepository = new GradesEventRepository(factory);
        this.usersEventRepository = new UsersEventRepository(factory);

        this.producer = new DisciplineProducer(kafka_bootstrap_server);

    }

    public void add(Context ctx) {
        try {
            String disciplineName = this.queryParam(ctx, "discipline_name");
            String description = ctx.queryParam("description"); // может быть null
            int supervisorId = this.parsePositiveInt(ctx, "supervisor_id");
            int semesterId = this.parsePositiveInt(ctx, "semester_id");
            int gradeId = this.parsePositiveInt(ctx, "grade_id");

            this.semestersEventRepository.getById(semesterId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", semesterId));

            this.gradesEventRepository.getById(gradeId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Уровень подготовки", gradeId));

            this.usersEventRepository.getById(supervisorId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Пользователь", supervisorId));


            Discipline discipline = new Discipline(0, disciplineName, supervisorId, description, semesterId, gradeId);
            this.producer.produceAdd(disciplineName, discipline);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.disciplinesEventRepository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Discipline discipline = this.disciplinesEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", id));
            new ObjectResponse<>(ctx, discipline);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Discipline discipline = this.disciplinesEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", id));

            new OkResponse(ctx);
            this.producer.produceDelete(String.valueOf(discipline.getId()), discipline);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            String disciplineName = this.queryParam(ctx, "discipline_name");
            String description = ctx.queryParam("description"); // может быть null
            int supervisorId = this.parsePositiveInt(ctx, "supervisor_id");
            int semesterId = this.parsePositiveInt(ctx, "semester_id");
            int gradeId = this.parsePositiveInt(ctx, "grade_id");

            Discipline existing = this.disciplinesEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", id));

            Discipline updatedDiscipline = new Discipline(id, disciplineName, supervisorId, description, semesterId, gradeId);
            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedDiscipline);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}
