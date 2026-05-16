package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.HomeworksEventRepository;
import ru.ayozav.models.Homework;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class HomeworksController {

    private final HomeworksEventRepository repository;

    public HomeworksController(HikariConnectionFactory factory) {
        this.repository = new HomeworksEventRepository(factory);
    }

    public void addHomework(Context ctx) {
        try {
            int lessonId = parsePositiveInt(ctx, "lesson_id");
            if (lessonId == -1) return;

            int semesterId = parsePositiveInt(ctx, "semester_id");
            if (semesterId == -1) return;

            LocalDateTime deadline = parseDateTime(ctx, "deadline");
            if (deadline == null) return;

            String description = ctx.queryParam("description");
            if (description == null) {
                description = ""; // or require non‑blank
            }

            String fileLink = ctx.queryParam("file_link"); // nullable

            int id = repository.add(lessonId, semesterId, deadline, description, fileLink);
            ctx.status(200).json(new SuccessObjectInsertAnswer("homework", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void getAllHomeworks(Context ctx) {
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

    public void getHomeworkById(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Homework> homework = repository.getById(id);
        if (homework.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(homework.get());
    }

    public void deleteHomework(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Homework> existing = repository.getById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer("Домашнее задание с id=" + id + " не найдено"));
            return;
        }

        try {
            repository.deleteById(id);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void updateHomework(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            int lessonId = parsePositiveInt(ctx, "lesson_id");
            if (lessonId == -1) return;

            int semesterId = parsePositiveInt(ctx, "semester_id");
            if (semesterId == -1) return;

            LocalDateTime deadline = parseDateTime(ctx, "deadline");
            if (deadline == null) return;

            String description = ctx.queryParam("description");
            if (description == null) description = "";

            String fileLink = ctx.queryParam("file_link");

            Optional<Homework> existing = repository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Домашнее задание с id=" + id + " не найдено"));
                return;
            }

            repository.update(id, lessonId, semesterId, deadline, description, fileLink);
            ctx.status(200).json(new SuccessUpdateAnswer("Домашнее задание", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

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

    private LocalDateTime parseDateTime(Context ctx, String paramName) {
        try {
            String dtStr = Objects.requireNonNull(ctx.queryParam(paramName));
            return LocalDateTime.parse(dtStr);
        } catch (DateTimeParseException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'" + paramName + "' должен быть в формате ГГГГ-ММ-ДДTЧЧ:ММ:СС"));
            return null;
        }
    }
}
