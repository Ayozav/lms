package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.HomeworksEventRepository;
import ru.ayozav.database.repositories.LessonsEventRepository;
import ru.ayozav.database.repositories.SemestersEventRepository;
import ru.ayozav.javalin.exceptions.*;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.HomeworkProducer;
import ru.ayozav.models.Homework;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HomeworksController extends ControllerSkeleton {

    private final HomeworksEventRepository repository;
    private final LessonsEventRepository lessonsRepository;
    private final SemestersEventRepository semestersRepository;
    private final HomeworkProducer producer;

    public HomeworksController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.repository = new HomeworksEventRepository(factory);
        this.lessonsRepository = new LessonsEventRepository(factory);
        this.semestersRepository = new SemestersEventRepository(factory);
        this.producer = new HomeworkProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            int lessonId = this.parsePositiveInt(ctx, "lesson_id");
            int semesterId = this.parsePositiveInt(ctx, "semester_id");
            LocalDate deadlineDate = this.parseDate("deadline_date", ctx.queryParam("deadline_date"));
            LocalTime deadlineTime = this.parseTime(ctx, "deadline_time");
            LocalDateTime deadline = deadlineDate.atTime(deadlineTime);
            String description = ctx.queryParam("description");
            if (description == null) description = ""; // или можно требовать не пустое через queryParam
            String fileLink = ctx.queryParam("file_link");

            // проверки существования занятия и семестра
            this.lessonsRepository.getById(lessonId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Занятие", lessonId));
            this.semestersRepository.getById(semesterId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", semesterId));

            Homework homework = new Homework(0, lessonId, semesterId, deadline, description, fileLink);
            this.producer.produceAdd(lessonId + "_" + semesterId + "_" + fileLink, homework);
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
            Homework homework = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Домашнее задание", id));
            new ObjectResponse<>(ctx, homework);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Homework homework = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Домашнее задание", id));
            this.producer.produceDelete(String.valueOf(homework.getId()), homework);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            int lessonId = this.parsePositiveInt(ctx, "lesson_id");
            int semesterId = this.parsePositiveInt(ctx, "semester_id");
            LocalDate deadlineDate = this.parseDate("deadline_date", ctx.queryParam("deadline_date"));
            LocalTime deadlineTime = this.parseTime(ctx, "deadline_time");
            LocalDateTime deadline = deadlineDate.atTime(deadlineTime);
            String description = ctx.queryParam("description");
            if (description == null) description = "";
            String fileLink = ctx.queryParam("file_link");

            Homework existing = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Домашнее задание", id));

            // проверки существования занятия и семестра
            this.lessonsRepository.getById(lessonId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Занятие", lessonId));
            this.semestersRepository.getById(semesterId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", semesterId));

            Homework updatedHomework = new Homework(id, lessonId, semesterId, deadline, description, fileLink);

            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedHomework);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}
