package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.MarksEventRepository;
import ru.ayozav.models.Mark;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class MarksController {

    private static final Set<String> VALID_ATTENDANCE_STATUSES = new HashSet<>(Arrays.asList(
            "present", "absent", "late", "excused"
    ));
    private final MarksEventRepository repository;

    public MarksController(HikariConnectionFactory factory) {
        this.repository = new MarksEventRepository(factory);
    }

    public void addMark(Context ctx) {
        try {
            int timetableId = parsePositiveInt(ctx, "timetable_id");
            if (timetableId == -1) return;

            int studentId = parsePositiveInt(ctx, "student_id");
            if (studentId == -1) return;

            LocalDate lessonRealDate = parseDate(ctx, "lesson_real_date");
            if (lessonRealDate == null) return;

            String attendanceStatus = ctx.queryParam("attendance_status");
            if (attendanceStatus == null || !VALID_ATTENDANCE_STATUSES.contains(attendanceStatus)) {
                ctx.status(400).json(new BadArgumentsAnswer(
                        "'attendance_status' должен быть одним из: " + VALID_ATTENDANCE_STATUSES
                ));
                return;
            }

            Integer mark = parseNullablePositiveInt(ctx, "mark");
            if (mark == null && ctx.queryParam("mark") != null) {
                // if param present but invalid, parseNullablePositiveInt already sent error response
                return;
            }

            int id = repository.add(timetableId, studentId, lessonRealDate, attendanceStatus, mark);
            ctx.status(200).json(new SuccessObjectInsertAnswer("mark", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void getAllMarks(Context ctx) {
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

    public void getMarkById(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Mark> mark = repository.getById(id);
        if (mark.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(mark.get());
    }

    public void deleteMark(Context ctx) {
        int id = parsePositiveInt(ctx, "id");
        if (id == -1) return;

        Optional<Mark> existing = repository.getById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer("Оценка/посещаемость с id=" + id + " не найдена"));
            return;
        }

        try {
            repository.deleteById(id);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void updateMark(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            int timetableId = parsePositiveInt(ctx, "timetable_id");
            if (timetableId == -1) return;

            int studentId = parsePositiveInt(ctx, "student_id");
            if (studentId == -1) return;

            LocalDate lessonRealDate = parseDate(ctx, "lesson_real_date");
            if (lessonRealDate == null) return;

            String attendanceStatus = ctx.queryParam("attendance_status");
            if (attendanceStatus == null || !VALID_ATTENDANCE_STATUSES.contains(attendanceStatus)) {
                ctx.status(400).json(new BadArgumentsAnswer(
                        "'attendance_status' должен быть одним из: " + VALID_ATTENDANCE_STATUSES
                ));
                return;
            }

            Integer mark = parseNullablePositiveInt(ctx, "mark");
            if (mark == null && ctx.queryParam("mark") != null) {
                return;
            }

            Optional<Mark> existing = repository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Оценка/посещаемость с id=" + id + " не найдена"));
                return;
            }

            repository.update(id, timetableId, studentId, lessonRealDate, attendanceStatus, mark);
            ctx.status(200).json(new SuccessUpdateAnswer("Оценка/посещаемость", id));

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

    private LocalDate parseDate(Context ctx, String paramName) {
        try {
            String dateStr = Objects.requireNonNull(ctx.queryParam(paramName));
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'" + paramName + "' должен быть в формате ГГГГ-ММ-ДД"));
            return null;
        }
    }

    private Integer parseNullablePositiveInt(Context ctx, String paramName) {
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
}
