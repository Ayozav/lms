package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.answers.BadArgumentsAnswer;
import ru.ayozav.answers.SuccessObjectInsertAnswer;
import ru.ayozav.answers.SuccessUpdateAnswer;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.database.repositories.GroupsEventRepository;
import ru.ayozav.models.Group;

import java.util.Objects;
import java.util.Optional;

public class GroupsController {

    private final GroupsEventRepository groupsEventRepository;

    public GroupsController(HikariConnectionFactory factory) {
        this.groupsEventRepository = new GroupsEventRepository(factory);
    }

    public void addGroup(Context ctx) {
        try {
            String groupName = ctx.queryParam("group_name");
            if (groupName == null || groupName.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'group_name' не может быть пустым"));
                return;
            }

            int headmanId;
            try {
                headmanId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("headman_id")));
                if (headmanId <= 0) throw new NumberFormatException();
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(400).json(new BadArgumentsAnswer("'headman_id' должен быть положительным целым числом"));
                return;
            }

            int firstSemesterId;
            try {
                firstSemesterId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("first_semester_id")));
                if (firstSemesterId <= 0) throw new NumberFormatException();
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(400).json(new BadArgumentsAnswer("'first_semester_id' должен быть положительным целым числом"));
                return;
            }

            int courseLevel;
            try {
                courseLevel = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("course_level")));
                if (courseLevel <= 0) throw new NumberFormatException();
            } catch (NumberFormatException | NullPointerException e) {
                ctx.status(400).json(new BadArgumentsAnswer("'course_level' должен быть положительным целым числом"));
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

            int id = this.groupsEventRepository.add(groupName, headmanId, firstSemesterId, courseLevel, gradeId);
            ctx.status(200).json(new SuccessObjectInsertAnswer("group", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer(
                    "Добавить группу (group_name=" + ctx.queryParam("group_name") + ") не вышло."
            ));
        }
    }

    public void getAllGroups(Context ctx) {
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
        ctx.status(200).json(this.groupsEventRepository.getList(page));
    }

    public void getGroupById(Context ctx) {
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

        Optional<Group> group = this.groupsEventRepository.getById(id);

        if (group.isEmpty()) {
            ctx.status(404);
            return;
        }
        ctx.status(200).json(group.get());
    }

    public void deleteGroup(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("id")));
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).json(new BadArgumentsAnswer("'id' должен быть целым положительным"));
            return;
        }

        Optional<Group> group = this.groupsEventRepository.getById(id);
        if (group.isEmpty()) {
            ctx.status(404).json(new BadArgumentsAnswer(
                    "Нет группы к удалению с 'id' " + id
            ));
            return;
        }

        this.groupsEventRepository.deleteById(id);
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

    public void updateGroup(Context ctx) {
        try {
            int id = parsePositiveInt(ctx, "id");
            if (id == -1) return;

            String groupName = ctx.queryParam("group_name");
            if (groupName == null || groupName.isBlank()) {
                ctx.status(400).json(new BadArgumentsAnswer("'group_name' не может быть пустым"));
                return;
            }

            int headmanId = parsePositiveInt(ctx, "headman_id");
            if (headmanId == -1) return;

            int firstSemesterId = parsePositiveInt(ctx, "first_semester_id");
            if (firstSemesterId == -1) return;

            int courseLevel = parsePositiveInt(ctx, "course_level");
            if (courseLevel == -1) return;

            int gradeId = parsePositiveInt(ctx, "grade_id");
            if (gradeId == -1) return;

            // optional: check existence
            Optional<Group> existing = groupsEventRepository.getById(id);
            if (existing.isEmpty()) {
                ctx.status(404).json(new BadArgumentsAnswer("Группа с id=" + id + " не найдена"));
                return;
            }

            groupsEventRepository.update(id, groupName, headmanId, firstSemesterId, courseLevel, gradeId);
            ctx.status(200).json(new SuccessUpdateAnswer("Группа", id));

        } catch (DatabaseException e) {
            ctx.status(400).json(new BadArgumentsAnswer("Не удалось обновить группу."));
        }
    }
}