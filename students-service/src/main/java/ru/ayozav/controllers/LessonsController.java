package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.LessonsEventRepository;
import ru.ayozav.models.Lesson;

import java.util.Objects;
import java.util.Optional;

public class LessonsController {

    private final LessonsEventRepository repository;

    public LessonsController(HikariConnectionFactory factory) {
        this.repository = new LessonsEventRepository(factory);
    }

    public void addLesson(Context ctx) {
        try {
            int disciplineId = parsePositiveInt(ctx, "discipline_id");
            if (disciplineId == -1) return;

            int orderedNumber = parsePositiveInt(ctx, "ordered_number");
            if (orderedNumber == -1) return;

            String mainTheme = ctx.queryParam("main_theme");
            if (mainTheme == null || mainTheme.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'main_theme' обязателен"));
                return;
            }

            String description = ctx.queryParam("description");
            if (description == null || description.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'description' обязателен"));
                return;
            }

            String teacherFileLink = ctx.queryParam("teacher_file_link"); // nullable
            String studentsFileLink = ctx.queryParam("students_file_link"); // nullable
            String type = ctx.queryParam("type");
            String format = ctx.queryParam("format");
            String recommendRoom = ctx.queryParam("recommend_room");

            Lesson lesson = new Lesson(0, disciplineId, orderedNumber, mainTheme, description,
                    teacherFileLink, studentsFileLink, type, format, recommendRoom);
            int id = repository.add(lesson);
            ctx.status(200).json(new SuccessObjectInsertAnswer("lesson", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось добавить занятие."));
        }
    }

    public void getAllLessons(Context ctx) {
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

    public void getLessonById(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Lesson> lesson = repository.getById(id);
        if (lesson.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(lesson.get());
    }

    public void deleteLesson(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Lesson> existing = repository.getById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer("Занятие с id=" + id + " не найдено"));
            return;
        }

        try {
            repository.deleteById(id);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void updateLesson(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            int disciplineId = parsePositiveInt(ctx, "discipline_id");
            if (disciplineId == -1) return;

            int orderedNumber = parsePositiveInt(ctx, "ordered_number");
            if (orderedNumber == -1) return;

            String mainTheme = ctx.queryParam("main_theme");
            if (mainTheme == null || mainTheme.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'main_theme' обязателен"));
                return;
            }

            String description = ctx.queryParam("description");
            if (description == null || description.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'description' обязателен"));
                return;
            }

            String teacherFileLink = ctx.queryParam("teacher_file_link");
            String studentsFileLink = ctx.queryParam("students_file_link");
            String type = ctx.queryParam("type");
            String format = ctx.queryParam("format");
            String recommendRoom = ctx.queryParam("recommend_room");

            Optional<Lesson> existing = repository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Занятие с id=" + id + " не найдено"));
                return;
            }

            Lesson lesson = new Lesson(id, disciplineId, orderedNumber, mainTheme, description,
                    teacherFileLink, studentsFileLink, type, format, recommendRoom);
            repository.update(lesson);
            ctx.status(200).json(new SuccessUpdateAnswer("Занятие", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Обновить занятие не вышло."));
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
}
