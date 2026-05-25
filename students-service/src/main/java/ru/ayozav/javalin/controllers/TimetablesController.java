package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.*;
import ru.ayozav.javalin.exceptions.*;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.TimetableProducer;
import ru.ayozav.models.Timetable;

import java.time.LocalTime;
import java.util.List;

public class TimetablesController extends ControllerSkeleton {

    private final TimetablesEventRepository timetablesEventRepository;
    private final SemestersEventRepository semestersEventRepository;
    private final DisciplinesEventRepository disciplinesEventRepository;
    private final UsersEventRepository usersEventRepository;
    private final GroupsEventRepository groupsEventRepository;

    private final TimetableProducer producer;

    public TimetablesController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.timetablesEventRepository = new TimetablesEventRepository(factory);
        this.semestersEventRepository = new SemestersEventRepository(factory);
        this.disciplinesEventRepository = new DisciplinesEventRepository(factory);
        this.usersEventRepository = new UsersEventRepository(factory);
        this.groupsEventRepository = new GroupsEventRepository(factory);

        this.producer = new TimetableProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            int semesterId = this.parsePositiveInt(ctx, "semester_id");
            int disciplineId = this.parsePositiveInt(ctx, "discipline_id");
            int teacherId = this.parsePositiveInt(ctx, "teacher_id");
            int dayOfWeek = this.parsePositiveInt(ctx, "day_of_week");
            int weekParity = this.parsePositiveInt(ctx, "week_parity");

            if (dayOfWeek < 1 || dayOfWeek > 7) {
                throw new ValueMustBeInRangeException("day_of_week", 1, 7);
            }
            if (weekParity != 0 && weekParity != 1) {
                throw new ValueMustBeInRangeException("week_parity", 0, 1);
            }

            String room = ctx.queryParam("room"); // может быть null

            LocalTime startTime = this.parseTime(ctx, "start_time");
            LocalTime endTime = this.parseTime(ctx, "end_time");

            if (startTime.isAfter(endTime)) {
                throw new DateRangeException();
            }

            // проверки существования связанных сущностей
            this.semestersEventRepository.getById(semesterId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", semesterId));
            this.disciplinesEventRepository.getById(disciplineId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", disciplineId));
            this.usersEventRepository.getById(teacherId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Преподаватель", teacherId));

            Timetable timetable = new Timetable(0, semesterId, disciplineId, teacherId,
                    dayOfWeek, weekParity, room, startTime, endTime);
            new OkResponse(ctx);

            this.producer.produceAdd(
                    String.valueOf(timetable.toString()),
                    timetable
            );

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.timetablesEventRepository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Timetable timetable = this.timetablesEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Расписание", id));
            new ObjectResponse<>(ctx, timetable);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getByGroup(Context ctx) {
        try {
            int groupId = this.parsePositiveInt(ctx, "group_id");
            int page = this.parsePositiveInt(ctx, "page");

            // Проверяем существование группы
            this.groupsEventRepository.getById(groupId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Группа", groupId));

            List<Timetable> timetables = this.timetablesEventRepository.getByGroup(groupId, page);
            new PageResponse<>(ctx, timetables);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    // Новый метод: расписание по преподавателю
    public void getByTeacher(Context ctx) {
        try {
            int teacherId = this.parsePositiveInt(ctx, "teacher_id");
            int page = this.parsePositiveInt(ctx, "page");

            // Проверяем существование преподавателя
            this.usersEventRepository.getById(teacherId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Преподаватель", teacherId));

            List<Timetable> timetables = this.timetablesEventRepository.getByTeacher(teacherId, page);
            new PageResponse<>(ctx, timetables);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Timetable timetable = this.timetablesEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Расписание", id));

            new OkResponse(ctx);
            this.producer.produceDelete(String.valueOf(timetable.getId()), timetable);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            int semesterId = this.parsePositiveInt(ctx, "semester_id");
            int disciplineId = this.parsePositiveInt(ctx, "discipline_id");
            int teacherId = this.parsePositiveInt(ctx, "teacher_id");
            int dayOfWeek = this.parsePositiveInt(ctx, "day_of_week");
            int weekParity = this.parsePositiveInt(ctx, "week_parity");

            if (dayOfWeek < 1 || dayOfWeek > 7) {
                throw new ValueMustBeInRangeException("day_of_week", 1, 7);
            }
            if (weekParity != 0 && weekParity != 1) {
                throw new ValueMustBeInRangeException("week_parity", 0, 1);
            }

            String room = ctx.queryParam("room");
            LocalTime startTime = this.parseTime(ctx, "start_time");
            LocalTime endTime = this.parseTime(ctx, "end_time");

            if (startTime.isAfter(endTime)) {
                throw new DateRangeException();
            }

            Timetable existing = this.timetablesEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Расписание", id));

            // проверки существования связанных сущностей
            this.semestersEventRepository.getById(semesterId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", semesterId));
            this.disciplinesEventRepository.getById(disciplineId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", disciplineId));
            this.usersEventRepository.getById(teacherId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Преподаватель", teacherId));

            Timetable updatedTimetable = new Timetable(id, semesterId, disciplineId, teacherId,
                    dayOfWeek, weekParity, room, startTime, endTime);
            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedTimetable);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}