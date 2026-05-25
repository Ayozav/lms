package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.TimetablesGroupsEventRepository;
import ru.ayozav.models.TimetableGroupLink;

import java.util.List;
import java.util.Objects;

public class TimetablesGroupsController {

    private final TimetablesGroupsEventRepository repository;

    public TimetablesGroupsController(HikariConnectionFactory factory) {
        this.repository = new TimetablesGroupsEventRepository(factory);
    }

    public void addLink(Context ctx) {
        try {
            int timetableId = parsePositiveInt(ctx, "timetable_id");
            if (timetableId == -1) return;

            int groupId = parsePositiveInt(ctx, "group_id");
            if (groupId == -1) return;

            repository.addLink(timetableId, groupId);
            ctx.status(200).json(new SuccessUpdateAnswer(
                    "Связь (timetableId=" + timetableId + ", groupId=" + groupId + ")",
                    timetableId
            ));
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось записать в расписание группу."));
        }
    }

    public void deleteLink(Context ctx) {
        try {
            int timetableId = parsePositiveInt(ctx, "timetable_id");
            if (timetableId == -1) return;

            int groupId = parsePositiveInt(ctx, "group_id");
            if (groupId == -1) return;

            repository.deleteLink(timetableId, groupId);
            ctx.status(200);
        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(e.getMessage()));
        }
    }

    public void getGroupsByTimetable(Context ctx) {
        int timetableId = parsePositiveInt(ctx, "timetable_id");
        if (timetableId == -1) return;

        List<TimetableGroupLink> groups = repository.getGroupsForTimetable(timetableId);
        ctx.status(200).json(groups);
    }

    public void getTimetablesByGroup(Context ctx) {
        int groupId = parsePositiveInt(ctx, "group_id");
        if (groupId == -1) return;

        List<TimetableGroupLink> timetables = repository.getTimetablesForGroup(groupId);
        ctx.status(200).json(timetables);
    }

    public void getAllLinks(Context ctx) {
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

    // Helper
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