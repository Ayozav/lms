package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.GradesEventRepository;
import ru.ayozav.models.Grade;
import ru.ayozav.models.GradeType;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class GradesController {
    private final GradesEventRepository gradesEventRepository;

    public GradesController(HikariConnectionFactory factory) {
        this.gradesEventRepository = new GradesEventRepository(factory);
    }

    public void addGrade(Context ctx) {
        try {
            String code = ctx.queryParam("code");
            String gradeName = ctx.queryParam("grade_name");
            String gradeType = ctx.queryParam("grade_type");
            if (!GradeType.exists(gradeType)) {
                ctx.status(400).json(
                        new BadArgumentsAnswer("'grad_type' должен быть из списка: " + Arrays.toString(GradeType.list()))
                );
                return;
            }
            int supervisorID;
            try {
                supervisorID = Integer.parseInt(
                        Objects.requireNonNull(
                                ctx.queryParam("supervisor_id")
                        )
                );
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(400).json(
                        new BadArgumentsAnswer(
                                "'supervisor_id' должен быть положительным целым числом.")
                );
                return;
            }
            int id = this.gradesEventRepository.add(code, gradeName, gradeType, supervisorID);
            ctx.status(200).json(new SuccessObjectInsertAnswer("grade", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(
                    new BadArgumentsAnswer(
                            "Добавить уровень подготовки (code=" +
                                    ctx.queryParam("code") +
                                    ") не вышло"
                    )
            );
        }
    }

    public void getAllGrades(Context ctx) {
        int page;
        try {
            page = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("page")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'page' должен быть целым положительным числом"));
            return;
        }

        if (page <= 0) {
            ctx.status(400).json(new BadArgumentsAnswer("'page' должен быть положительным."));
            return;
        }
        ctx.status(200).json(this.gradesEventRepository.getList(page));

    }

    public void getGradeById(Context ctx) {
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

        Optional<Grade> grade = this.gradesEventRepository.getById(id);

        if (grade.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(grade.get());
    }

    public void deleteGrade(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }

        Optional<Grade> user = this.gradesEventRepository.getById(id);
        if (user.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer(
                    "Нет grade к удалению с 'id' " + id
            ));
            return;
        }

        this.gradesEventRepository.deleteById(id);
        ctx.status(200);
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

    public void updateGrade(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            String code = ctx.queryParam("code");
            if (code == null || code.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'code' не может быть пустым"));
                return;
            }

            String gradeName = ctx.queryParam("grade_name");
            if (gradeName == null || gradeName.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'grade_name' не может быть пустым"));
                return;
            }

            String gradeType = ctx.queryParam("grade_type");
            if (!GradeType.exists(gradeType)) {
                ctx.status(400).json(new BadArgumentsAnswer("'grade_type' должен быть из: " + Arrays.toString(GradeType.list())));
                return;
            }

            int supervisorID = parsePositiveInt(ctx, "supervisor_id");
            if (supervisorID == -1) return;

            Optional<Grade> existing = gradesEventRepository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Уровень подготовки с id=" + id + " не найден"));
                return;
            }

            gradesEventRepository.update(id, code, gradeName, gradeType, supervisorID);
            ctx.status(200).json(new SuccessUpdateAnswer("Уровень подготовки", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось обновить уровень подготовки: " + e.getMessage()));
        }
    }
}
