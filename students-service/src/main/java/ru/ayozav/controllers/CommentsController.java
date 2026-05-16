package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.CommentsEventRepository;
import ru.ayozav.models.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class CommentsController {

    private final CommentsEventRepository repository;

    public CommentsController(HikariConnectionFactory factory) {
        this.repository = new CommentsEventRepository(factory);
    }

    public void addComment(Context ctx) {
        try {
            int attachedHomeworkId = parsePositiveInt(ctx, "attached_homework_id");
            if (attachedHomeworkId == -1) return;

            int fromId = parsePositiveInt(ctx, "from_id");
            if (fromId == -1) return;

            String message = ctx.queryParam("message");
            if (message == null || message.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'message' не может быть пустым"));
                return;
            }

            // send_time: if not provided, use current time
            LocalDateTime sendTime = parseOptionalDateTime(ctx, "send_time");
            if (sendTime == null && ctx.queryParam("send_time") != null) return;
            if (sendTime == null) sendTime = LocalDateTime.now();

            int id = repository.add(attachedHomeworkId, fromId, sendTime, message);
            ctx.status(200).json(new SuccessObjectInsertAnswer("comment", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void getAllComments(Context ctx) {
        int page;
        try {
            page = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("page")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'page' должен быть целым положительным числом"));
            return;
        }
        if (page <= 0) {
            ctx.status(400).json(new BadArgumentsAnswer("'page' должен быть положительным"));
            return;
        }
        ctx.status(200).json(repository.getList(page));
    }

    public void getCommentById(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Comment> comment = repository.getById(id);
        if (comment.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(comment.get());
    }

    public void deleteComment(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Comment> existing = repository.getById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer("Комментарий с id=" + id + " не найден"));
            return;
        }

        try {
            repository.deleteById(id);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void updateComment(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            int attachedHomeworkId = parsePositiveInt(ctx, "attached_homework_id");
            if (attachedHomeworkId == -1) return;

            int fromId = parsePositiveInt(ctx, "from_id");
            if (fromId == -1) return;

            String message = ctx.queryParam("message");
            if (message == null || message.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'message' не может быть пустым"));
                return;
            }

            LocalDateTime sendTime = parseOptionalDateTime(ctx, "send_time");
            if (sendTime == null && ctx.queryParam("send_time") != null) return;
            if (sendTime == null) sendTime = LocalDateTime.now();

            Optional<Comment> existing = repository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Комментарий с id=" + id + " не найден"));
                return;
            }

            repository.update(id, attachedHomeworkId, fromId, sendTime, message);
            ctx.status(200).json(new SuccessUpdateAnswer("Комментарий", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    // Helper methods
    private int parsePositiveInt(Context ctx, String paramName) {
        try {
            int value = Integer.parseInt(Objects.requireNonNull(ctx.queryParam(paramName)));
            if (value <= 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'" + paramName + "' должен быть положительным целым числом"));
            return -1;
        }
    }

    private LocalDateTime parseOptionalDateTime(Context ctx, String paramName) {
        String dtStr = ctx.queryParam(paramName);
        if (dtStr == null || dtStr.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dtStr);
        } catch (DateTimeParseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'" + paramName + "' должен быть в формате ГГГГ-ММ-ДДTЧЧ:ММ:СС"));
            return null;
        }
    }
}