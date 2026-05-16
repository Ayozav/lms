package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.DisciplinesEventRepository;
import ru.ayozav.models.Discipline;

import java.util.Objects;
import java.util.Optional;

public class DisciplinesController {

    private final DisciplinesEventRepository disciplinesEventRepository;

    public DisciplinesController(HikariConnectionFactory factory) {
        this.disciplinesEventRepository = new DisciplinesEventRepository(factory);
    }

    public void addDiscipline(Context ctx) {
        try {
            String disciplineName = ctx.queryParam("discipline_name");
            if (disciplineName == null || disciplineName.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'discipline_name' не может быть пустым"));
                return;
            }

            String description = ctx.queryParam("description"); // may be null

            int supervisorId;
            try {
                supervisorId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("supervisor_id")));
                if (supervisorId <= 0) throw new NumberFormatException();
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(400).json(new BadArgumentsAnswer("'supervisor_id' должен быть положительным целым числом"));
                return;
            }

            int semesterId;
            try {
                semesterId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("semester_id")));
                if (semesterId <= 0) throw new NumberFormatException();
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(400).json(new BadArgumentsAnswer("'semester_id' должен быть положительным целым числом"));
                return;
            }

            int gradeId;
            try {
                gradeId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("grade_id")));
                if (gradeId <= 0) throw new NumberFormatException();
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(400).json(new BadArgumentsAnswer("'grade_id' должен быть положительным целым числом"));
                return;
            }

            int id = this.disciplinesEventRepository.add(disciplineName, supervisorId, description, semesterId, gradeId);
            ctx.status(200).json(new SuccessObjectInsertAnswer("discipline", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(
                    "Добавить дисциплину (discipline_name=" + ctx.queryParam("discipline_name") + ") не вышло"
            ));
        }
    }

    public void getAllDisciplines(Context ctx) {
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
        ctx.status(200).json(this.disciplinesEventRepository.getList(page));
    }

    public void getDisciplineById(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }
        if (id <= 0) {
            ctx.status(404);
            return;
        }

        Optional<Discipline> discipline = this.disciplinesEventRepository.getById(id);

        if (discipline.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(discipline.get());
    }

    public void deleteDiscipline(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }

        Optional<Discipline> discipline = this.disciplinesEventRepository.getById(id);
        if (discipline.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer(
                    "Нет дисциплины к удалению с 'id' " + id
            ));
            return;
        }

        this.disciplinesEventRepository.deleteById(id);
        ctx.status(200);
    }
}