package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.TimetablesEventRepository;
import ru.ayozav.models.Timetable;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class TimetablesController {

    private final TimetablesEventRepository timetablesEventRepository;

    public TimetablesController(HikariConnectionFactory factory) {
        this.timetablesEventRepository = new TimetablesEventRepository(factory);
    }

    public void addTimetable(Context ctx) {
        try {
            // Parse integer fields
            int semesterId = parsePositiveInt(ctx, "semester_id");
            if (semesterId == -1) return;

            int disciplineId = parsePositiveInt(ctx, "discipline_id");
            if (disciplineId == -1) return;

            int teacherId = parsePositiveInt(ctx, "teacher_id");
            if (teacherId == -1) return;

            int dayOfWeek = parsePositiveInt(ctx, "day_of_week");
            if (dayOfWeek == -1) return;
            if (dayOfWeek < 1 || dayOfWeek > 7) {
                ctx.status(400).json(new BadArgumentsAnswer("'day_of_week' должен быть от 1 (пн) до 7 (вс)"));
                return;
            }

            int weekParity = parsePositiveInt(ctx, "week_parity");
            if (weekParity == -1) return;
            if (weekParity != 0 && weekParity != 1) {
                ctx.status(400).json(new BadArgumentsAnswer("'week_parity' должен быть 0 (чётная) или 1 (нечётная)"));
                return;
            }

            String room = ctx.queryParam("room"); // may be null, allowed

            LocalTime startTime = parseTime(ctx, "start_time");
            if (startTime == null) return;

            LocalTime endTime = parseTime(ctx, "end_time");
            if (endTime == null) return;

            if (startTime.isAfter(endTime)) {
                ctx.status(400).json(new BadArgumentsAnswer("'start_time' не может быть позже 'end_time'"));
                return;
            }

            int id = this.timetablesEventRepository.add(semesterId, disciplineId, teacherId,
                    dayOfWeek, weekParity, room, startTime, endTime);
            ctx.status(200).json(new SuccessObjectInsertAnswer("timetable", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(
                    "Добавить запись расписания не вышло."
            ));
        }
    }

    public void getAllTimetables(Context ctx) {
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
        ctx.status(200).json(this.timetablesEventRepository.getList(page));
    }

    public void getTimetableById(Context ctx) {
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

        Optional<Timetable> timetable = this.timetablesEventRepository.getById(id);

        if (timetable.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(timetable.get());
    }

    public void deleteTimetable(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }

        Optional<Timetable> timetable = this.timetablesEventRepository.getById(id);
        if (timetable.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer(
                    "Нет записи расписания к удалению с 'id' " + id
            ));
            return;
        }

        this.timetablesEventRepository.deleteById(id);
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

    private LocalTime parseTime(Context ctx, String paramName) {
        try {
            String timeStr = Objects.requireNonNull(ctx.queryParam(paramName));
            return LocalTime.parse(timeStr);
        } catch (DateTimeParseException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'" + paramName + "' должен быть в формате HH:MM[:SS]"));
            return null;
        }
    }
}