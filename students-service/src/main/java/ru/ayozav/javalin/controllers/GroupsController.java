package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.GroupsEventRepository;
import ru.ayozav.database.repositories.SemestersEventRepository;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.database.repositories.GradesEventRepository;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.GroupProducer;
import ru.ayozav.models.Group;

public class GroupsController extends ControllerSkeleton {

    private final GroupsEventRepository groupsEventRepository;
    private final UsersEventRepository usersEventRepository;
    private final SemestersEventRepository semestersEventRepository;
    private final GradesEventRepository gradesEventRepository;
    private final GroupProducer producer;

    public GroupsController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.groupsEventRepository = new GroupsEventRepository(factory);
        this.usersEventRepository = new UsersEventRepository(factory);
        this.semestersEventRepository = new SemestersEventRepository(factory);
        this.gradesEventRepository = new GradesEventRepository(factory);
        this.producer = new GroupProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            String groupName = this.queryParam(ctx, "group_name");
            int headmanId = this.parsePositiveInt(ctx, "headman_id");
            int firstSemesterId = this.parsePositiveInt(ctx, "first_semester_id");
            int courseLevel = this.parsePositiveInt(ctx, "course_level");
            int gradeId = this.parsePositiveInt(ctx, "grade_id");

            // проверки существования связанных сущностей
            this.getUser(headmanId, usersEventRepository);
            this.semestersEventRepository.getById(firstSemesterId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", firstSemesterId));
            this.gradesEventRepository.getById(gradeId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Уровень подготовки", gradeId));

            Group group = new Group(0, groupName, headmanId, firstSemesterId, courseLevel, gradeId);
            new OkResponse(ctx);
            this.producer.produceAdd(String.valueOf(group.getHeadmanId()), group);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.groupsEventRepository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Group group = this.groupsEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Группа", id));
            new ObjectResponse<>(ctx, group);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Group group = this.groupsEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Группа", id));

            new OkResponse(ctx);
            this.producer.produceDelete(String.valueOf(group.getId()), group);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            String groupName = this.queryParam(ctx, "group_name");
            int headmanId = this.parsePositiveInt(ctx, "headman_id");
            int firstSemesterId = this.parsePositiveInt(ctx, "first_semester_id");
            int courseLevel = this.parsePositiveInt(ctx, "course_level");
            int gradeId = this.parsePositiveInt(ctx, "grade_id");

            Group existing = this.groupsEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Группа", id));

            // проверки существования связанных сущностей
            this.getUser(headmanId, usersEventRepository);
            this.semestersEventRepository.getById(firstSemesterId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", firstSemesterId));
            this.gradesEventRepository.getById(gradeId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Уровень подготовки", gradeId));

            Group updatedGroup = new Group(id, groupName, headmanId, firstSemesterId, courseLevel, gradeId);
            new OkResponse(ctx);
            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedGroup);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}