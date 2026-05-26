package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.DisciplinesEventRepository;
import ru.ayozav.database.repositories.LessonsEventRepository;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.exceptions.ParamMustBeException;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.LessonProducer;
import ru.ayozav.models.Lesson;


public class LessonsController extends ControllerSkeleton {

    private final LessonsEventRepository repository;
    private final DisciplinesEventRepository disciplinesRepository;
    private final LessonProducer producer;

    public LessonsController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.repository = new LessonsEventRepository(factory);
        this.disciplinesRepository = new DisciplinesEventRepository(factory);
        this.producer = new LessonProducer(kafka_bootstrap_server);
    }

    // вспомогательный метод для обязательных строковых параметров
    private String queryNonBlankParam(Context ctx, String paramName) throws ParamMustBeException {
        String value = this.queryParam(ctx, paramName);
        if (value == null || value.isBlank()) {
            throw new ParamMustBeException(paramName + " не может быть пустым");
        }
        return value;
    }

    public void add(Context ctx) {
        try {
            int disciplineId = this.parsePositiveInt(ctx, "discipline_id");
            int orderedNumber = this.parsePositiveInt(ctx, "ordered_number");
            String mainTheme = this.queryNonBlankParam(ctx, "main_theme");
            String description = this.queryNonBlankParam(ctx, "description");
            String teacherFileLink = ctx.queryParam("teacher_file_link");
            String studentsFileLink = ctx.queryParam("students_file_link");
            String type = ctx.queryParam("type");
            String format = ctx.queryParam("format");
            String recommendRoom = ctx.queryParam("recommend_room");

            // проверка существования дисциплины
            this.disciplinesRepository.getById(disciplineId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", disciplineId));

            Lesson lesson = new Lesson(0, disciplineId, orderedNumber, mainTheme, description,
                    teacherFileLink, studentsFileLink, type, format, recommendRoom);
            this.producer.produceAdd(lesson.toString(), lesson);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.repository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Lesson lesson = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Занятие", id));
            new ObjectResponse<>(ctx, lesson);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Lesson lesson = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Занятие", id));

            this.producer.produceDelete(String.valueOf(lesson.getId()), lesson);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            int disciplineId = this.parsePositiveInt(ctx, "discipline_id");
            int orderedNumber = this.parsePositiveInt(ctx, "ordered_number");
            String mainTheme = this.queryNonBlankParam(ctx, "main_theme");
            String description = this.queryNonBlankParam(ctx, "description");
            String teacherFileLink = ctx.queryParam("teacher_file_link");
            String studentsFileLink = ctx.queryParam("students_file_link");
            String type = ctx.queryParam("type");
            String format = ctx.queryParam("format");
            String recommendRoom = ctx.queryParam("recommend_room");

            Lesson existing = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Занятие", id));

            // проверка существования дисциплины
            this.disciplinesRepository.getById(disciplineId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", disciplineId));

            Lesson updatedLesson = new Lesson(id, disciplineId, orderedNumber, mainTheme, description,
                    teacherFileLink, studentsFileLink, type, format, recommendRoom);
            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedLesson);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}