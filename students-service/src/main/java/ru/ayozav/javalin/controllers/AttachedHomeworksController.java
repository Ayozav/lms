package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.AttachedHomeworksEventRepository;
import ru.ayozav.database.repositories.HomeworksEventRepository;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.*;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.AttachedHomeworkProducer;
import ru.ayozav.models.AttachedHomework;

import java.time.LocalDateTime;

public class AttachedHomeworksController extends ControllerSkeleton {

    private final AttachedHomeworksEventRepository repository;
    private final HomeworksEventRepository homeworksRepository;
    private final UsersEventRepository usersRepository;
    private final AttachedHomeworkProducer producer;

    public AttachedHomeworksController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.repository = new AttachedHomeworksEventRepository(factory);
        this.homeworksRepository = new HomeworksEventRepository(factory);
        this.usersRepository = new UsersEventRepository(factory);
        this.producer = new AttachedHomeworkProducer(kafka_bootstrap_server);
    }


    public void add(Context ctx) {
        try {
            int homeworkId = this.parsePositiveInt(ctx, "homework_id");
            int studentId = this.parsePositiveInt(ctx, "student_id");
            int mark = parsePositiveInt(ctx, "mark");

            // проверки существования домашнего задания и студента
            this.homeworksRepository.getById(homeworkId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Домашнее задание", homeworkId));
            this.usersRepository.getById(studentId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Студент", studentId));

            LocalDateTime attachDate = LocalDateTime.now();

            AttachedHomework attachment = new AttachedHomework(0, homeworkId, studentId, mark, attachDate);
            this.producer.produceAdd(homeworkId + "_" + studentId + "_" + mark + "_" + attachDate, attachment);
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
            AttachedHomework attachment = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Сданное домашнее задание", id));
            new ObjectResponse<>(ctx, attachment);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            AttachedHomework attachment = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Сданное домашнее задание", id));

            this.producer.produceDelete(String.valueOf(attachment.getId()), attachment);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            int homeworkId = this.parsePositiveInt(ctx, "homework_id");
            int studentId = this.parsePositiveInt(ctx, "student_id");
            int mark = parsePositiveInt(ctx, "mark");

            AttachedHomework existing = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Сданное домашнее задание", id));

            // проверки существования домашнего задания и студента
            this.homeworksRepository.getById(homeworkId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Домашнее задание", homeworkId));
            this.usersRepository.getById(studentId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Студент", studentId));

            // attachDate не обновляется – остаётся из существующей записи
            AttachedHomework updatedAttachment = new AttachedHomework(id, homeworkId, studentId, mark, existing.getAttachDate());

            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedAttachment);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}