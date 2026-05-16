package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.SemestersEventRepository;
import ru.ayozav.models.Semester;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

public class SemestersController {

    private final SemestersEventRepository semestersEventRepository;

    public SemestersController(HikariConnectionFactory factory) {
        this.semestersEventRepository = new SemestersEventRepository(factory);
    }

    public void addSemester(Context ctx) {
        try {
            String name = ctx.queryParam("name");
            if (name == null || name.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'name' не может быть пустым"));
                return;
            }


            String startStr = ctx.queryParam("start");
            String endStr = ctx.queryParam("end");
            LocalDate start, end;
            try {
                start = LocalDate.parse(Objects.requireNonNull(startStr));
                end = LocalDate.parse(Objects.requireNonNull(endStr));
            } catch (DateTimeParseException e) {
                ctx.status(400).json(new BadArgumentsAnswer(
                        "'start' и 'end' должны быть в формате ГГГГ-ММ-ДД"
                ));
                return;
            }

            if (start.isAfter(end)) {
                ctx.status(400).json(new BadArgumentsAnswer(
                        "'start' не может быть позже 'end'"
                ));
                return;
            }

            int id = this.semestersEventRepository.add(name, start, end);
            ctx.status(200).json(new SuccessObjectInsertAnswer("semester", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(
                    "Добавить семестр (name=" + ctx.queryParam("name") + ") не вышло."
            ));
        }
    }

    public void getAllSemesters(Context ctx) {
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
        ctx.status(200).json(this.semestersEventRepository.getList(page));
    }

    public void getSemesterById(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }
        catch (NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Должен быть параметр 'id'"));
            return;
        }
        if (id <= 0) {
            ctx.status(404);
            return;
        }

        Optional<Semester> semester = this.semestersEventRepository.getById(id);

        if (semester.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(semester.get());
    }

    public void deleteSemester(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }
        catch (NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Должен быть параметр 'id'"));
            return;
        }

        Optional<Semester> semester = this.semestersEventRepository.getById(id);
        if (semester.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer(
                    "Нет семестра к удалению с 'id' " + id
            ));
            return;
        }

        this.semestersEventRepository.deleteById(id);
        ctx.status(200);
    }
}