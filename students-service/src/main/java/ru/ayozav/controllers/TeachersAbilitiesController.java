package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.TeachersAbilitiesEventRepository;
import ru.ayozav.models.TeachersAbility;

import java.util.List;
import java.util.Objects;

public class TeachersAbilitiesController {

    private final TeachersAbilitiesEventRepository repository;

    public TeachersAbilitiesController(HikariConnectionFactory factory) {
        this.repository = new TeachersAbilitiesEventRepository(factory);
    }

    public void addAbility(Context ctx) {
        try {
            int teacherId = parsePositiveInt(ctx, "teacher_id");
            if (teacherId == -1) return;

            int disciplineId = parsePositiveInt(ctx, "discipline_id");
            if (disciplineId == -1) return;

            repository.addLink(teacherId, disciplineId);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось добавить возможность преподавателю."));
        }
    }

    public void deleteAbility(Context ctx) {
        try {
            int teacherId = parsePositiveInt(ctx, "teacher_id");
            if (teacherId == -1) return;

            int disciplineId = parsePositiveInt(ctx, "discipline_id");
            if (disciplineId == -1) return;

            repository.deleteLink(teacherId, disciplineId);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось удалить возможность преподавателю."));
        }
    }

    public void getDisciplinesByTeacher(Context ctx) {
        int teacherId = parsePositiveInt(ctx, "teacher_id");
        if (teacherId == -1) return;

        List<TeachersAbility> abilities = repository.getDisciplinesForTeacher(teacherId);
        ctx.status(200).json(abilities);
    }

    public void getTeachersByDiscipline(Context ctx) {
        int disciplineId = parsePositiveInt(ctx, "discipline_id");
        if (disciplineId == -1) return;

        List<TeachersAbility> abilities = repository.getTeachersForDiscipline(disciplineId);
        ctx.status(200).json(abilities);
    }

    public void getAllAbilities(Context ctx) {
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
        ctx.status(200).json(repository.getPage(page));
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