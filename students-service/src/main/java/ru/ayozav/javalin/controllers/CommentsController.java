package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.AttachedHomeworksEventRepository;
import ru.ayozav.database.repositories.CommentsEventRepository;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.*;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.CommentProducer;
import ru.ayozav.models.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CommentsController extends ControllerSkeleton {

    private final CommentsEventRepository repository;
    private final AttachedHomeworksEventRepository attachedHomeworksRepository;
    private final UsersEventRepository usersRepository;
    private final CommentProducer producer;

    public CommentsController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.repository = new CommentsEventRepository(factory);
        this.attachedHomeworksRepository = new AttachedHomeworksEventRepository(factory);
        this.usersRepository = new UsersEventRepository(factory);
        this.producer = new CommentProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            int attachedHomeworkId = this.parsePositiveInt(ctx, "attached_homework_id");
            int fromId = this.parsePositiveInt(ctx, "from_id");
            String message = this.queryParam(ctx, "message");
            if (message == null || message.isBlank()) {
                throw new ParamMustBeException("message не может быть пустым");
            }

            // проверки существования привязанного домашнего задания и отправителя
            this.attachedHomeworksRepository.getById(attachedHomeworkId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Привязанное домашнее задание", attachedHomeworkId));
            this.usersRepository.getById(fromId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Пользователь", fromId));

            LocalDate sendDate = parseDate("send_time", ctx.queryParam("send_date"));
            LocalTime sendTime_ = parseTime(ctx, "send_time");
            LocalDateTime sendTime = sendDate.atTime(sendTime_);

            Comment comment = new Comment(0, attachedHomeworkId, fromId, sendTime, message);
            this.producer.produceAdd(attachedHomeworkId + "_" + fromId + "_" + sendTime, comment);
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
            Comment comment = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Комментарий", id));
            new ObjectResponse<>(ctx, comment);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Comment comment = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Комментарий", id));
            this.producer.produceDelete(String.valueOf(comment.getId()), comment);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            int attachedHomeworkId = this.parsePositiveInt(ctx, "attached_homework_id");
            int fromId = this.parsePositiveInt(ctx, "from_id");
            String message = this.queryParam(ctx, "message");
            if (message == null || message.isBlank()) {
                throw new ParamMustBeException("message не может быть пустым");
            }

            Comment existing = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Комментарий", id));

            // проверки существования сущностей
            this.attachedHomeworksRepository.getById(attachedHomeworkId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Привязанное домашнее задание", attachedHomeworkId));
            this.usersRepository.getById(fromId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Пользователь", fromId));

            LocalDate sendDate = parseDate("send_time", ctx.queryParam("send_date"));
            LocalTime sendTime_ = parseTime(ctx, "send_time");
            LocalDateTime sendTime = sendDate.atTime(sendTime_);

            Comment updatedComment = new Comment(id, attachedHomeworkId, fromId, sendTime, message);

            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedComment);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}