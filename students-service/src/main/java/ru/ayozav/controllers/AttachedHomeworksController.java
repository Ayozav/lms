package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.AttachedHomeworksEventRepository;
import ru.ayozav.models.AttachedHomework;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class AttachedHomeworksController {

    private final AttachedHomeworksEventRepository repository;

    public AttachedHomeworksController(HikariConnectionFactory factory) {
        this.repository = new AttachedHomeworksEventRepository(factory);
    }

    public void addAttachedHomework(Context ctx) {
        try {
            int homeworkId = parsePositiveInt(ctx, "homework_id");
            if (homeworkId == -1) return;

            int studentId = parsePositiveInt(ctx, "student_id");
            if (studentId == -1) return;

            Integer mark = parseNullableNonNegativeInt(ctx, "mark");
            if (mark == null && ctx.queryParam("mark") != null) return;

            // attach_date: if not provided, use current time
            LocalDateTime attachDate = LocalDateTime.now();

            int id = repository.add(homeworkId, studentId, mark, attachDate);
            ctx.status(200).json(new SuccessObjectInsertAnswer("attached_homework", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void getAllAttachedHomeworks(Context ctx) {
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

    public void getAttachedHomeworkById(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<AttachedHomework> attachment = repository.getById(id);
        if (attachment.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(attachment.get());
    }

    public void deleteAttachedHomework(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<AttachedHomework> existing = repository.getById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer("Сданное домашнее задание с id=" + id + " не найдено"));
            return;
        }

        try {
            repository.deleteById(id);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void updateAttachedHomework(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            int homeworkId = parsePositiveInt(ctx, "homework_id");
            if (homeworkId == -1) return;

            int studentId = parsePositiveInt(ctx, "student_id");
            if (studentId == -1) return;

            Integer mark = parseNullableNonNegativeInt(ctx, "mark");
            if (mark == null && ctx.queryParam("mark") != null) return;

            LocalDateTime attachDate = parseOptionalDateTime(ctx, "attach_date");
            if (attachDate == null && ctx.queryParam("attach_date") != null) return;
            if (attachDate == null) attachDate = LocalDateTime.now();

            Optional<AttachedHomework> existing = repository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Сданное домашнее задание с id=" + id + " не найдено"));
                return;
            }

            repository.update(id, homeworkId, studentId, mark, attachDate);
            ctx.status(200).json(new SuccessUpdateAnswer("Сданное домашнее задание", id));

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

    private Integer parseNullableNonNegativeInt(Context ctx, String paramName) {
        String valueStr = ctx.queryParam(paramName);
        if (valueStr == null || valueStr.isBlank()) {
            return null;
        }
        try {
            int value = Integer.parseInt(valueStr);
            if (value < 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'" + paramName + "' должен быть неотрицательным целым числом"));
            return null;
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