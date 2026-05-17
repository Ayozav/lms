package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.EnrollmentsEventRepository;
import ru.ayozav.models.Enrollment;

import java.util.Objects;
import java.util.Optional;

public class EnrollmentsController {

    private final EnrollmentsEventRepository enrollmentsEventRepository;

    public EnrollmentsController(HikariConnectionFactory factory) {
        this.enrollmentsEventRepository = new EnrollmentsEventRepository(factory);
    }

    public void addEnrollment(Context ctx) {
        try {
            int studentId = parsePositiveInt(ctx, "student_id");
            if (studentId == -1) return;

            int groupId = parsePositiveInt(ctx, "group_id");
            if (groupId == -1) return;

            int startSemesterId = parsePositiveInt(ctx, "start_semester_id");
            if (startSemesterId == -1) return;

            Integer endSemesterId = parseNullablePositiveInt(ctx, "end_semester_id");

            int id = enrollmentsEventRepository.add(studentId, groupId, startSemesterId, endSemesterId);
            ctx.status(200).json(new SuccessObjectInsertAnswer("enrollment", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось добавить зачисление."));
        }
    }

    public void getAllEnrollments(Context ctx) {
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
        ctx.status(200).json(enrollmentsEventRepository.getList(page));
    }

    public void getEnrollmentById(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Enrollment> enrollment = enrollmentsEventRepository.getById(id);
        if (enrollment.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(enrollment.get());
    }

    public void deleteEnrollment(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Enrollment> existing = enrollmentsEventRepository.getById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer("Нет записи о зачислении с id=" + id));
            return;
        }

        enrollmentsEventRepository.deleteById(id);
        ctx.status(200);
    }

    public void updateEnrollment(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            int studentId = parsePositiveInt(ctx, "student_id");
            if (studentId == -1) return;

            int groupId = parsePositiveInt(ctx, "group_id");
            if (groupId == -1) return;

            int startSemesterId = parsePositiveInt(ctx, "start_semester_id");
            if (startSemesterId == -1) return;

            Integer endSemesterId = parseNullablePositiveInt(ctx, "end_semester_id");

            Optional<Enrollment> existing = enrollmentsEventRepository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Запись о зачислении с id=" + id + " не найдена"));
                return;
            }

            enrollmentsEventRepository.update(id, studentId, groupId, startSemesterId, endSemesterId);
            ctx.status(200).json(new SuccessUpdateAnswer("Зачисление", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось обновить зачисление."));
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

    private Integer parseNullablePositiveInt(Context ctx, String paramName) {
        String paramValue = ctx.queryParam(paramName);
        if (paramValue == null || paramValue.isBlank()) {
            return null;
        }
        try {
            int value = Integer.parseInt(paramValue);
            if (value <= 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'" + paramName + "' должен быть положительным целым числом или пустым"));
            return null; // but we need to indicate error; we'll return -2 to differentiate
        }
    }
}