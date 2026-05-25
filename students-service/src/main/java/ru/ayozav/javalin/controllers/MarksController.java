package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.MarksEventRepository;
import ru.ayozav.database.repositories.TimetablesEventRepository;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.*;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.MarkProducer;
import ru.ayozav.models.AttendanceStatuses;
import ru.ayozav.models.Mark;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class MarksController extends ControllerSkeleton {

    private final MarksEventRepository repository;
    private final TimetablesEventRepository timetablesRepository;
    private final UsersEventRepository usersRepository;
    private final MarkProducer producer;

    public MarksController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.repository = new MarksEventRepository(factory);
        this.timetablesRepository = new TimetablesEventRepository(factory);
        this.usersRepository = new UsersEventRepository(factory);
        this.producer = new MarkProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            int timetableId = this.parsePositiveInt(ctx, "timetable_id");
            int studentId = this.parsePositiveInt(ctx, "student_id");
            LocalDate lessonRealDate = this.parseDate(this.queryParam(ctx, "lesson_real_date"));

            String attendanceStatus = this.queryParam(ctx, "attendance_status");
            if (!AttendanceStatuses.exists(attendanceStatus)) {
                throw new ValueMustBeFromEnum("attendance_status", attendanceStatus, AttendanceStatuses.class);
            }

            int mark = parsePositiveInt(ctx, "mark");

            // проверки существования расписания и студента
            this.timetablesRepository.getById(timetableId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Расписание", timetableId));
            this.usersRepository.getById(studentId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Студент", studentId));

            Mark markObj = new Mark(0, timetableId, studentId, lessonRealDate,
                    LocalDateTime.now(), attendanceStatus, mark);

            this.producer.produceAdd(
                    timetableId + "_" + studentId + "_" + lessonRealDate,
                    markObj
            );
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.repository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Mark mark = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Оценка/посещаемость", id));

            new ObjectResponse<>(ctx, mark);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getByStudentID(Context ctx) {
        try {
            int studentID = this.parsePositiveInt(ctx, "student_id");
            LocalDate start = this.parseDate(ctx.queryParam("start_date"));
            LocalDate end = this.parseDate(ctx.queryParam("end_date"));

            new ObjectResponse<>(ctx, this.repository.getByStudent(studentID, start, end));
        }
        catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());

        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Mark mark = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Оценка/посещаемость", id));

            this.producer.produceDelete(String.valueOf(mark.getId()), mark);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            int timetableId = this.parsePositiveInt(ctx, "timetable_id");
            int studentId = this.parsePositiveInt(ctx, "student_id");
            LocalDate lessonRealDate = this.parseDate(this.queryParam(ctx, "lesson_real_date"));

            String attendanceStatus = this.queryParam(ctx, "attendance_status");
            if (!AttendanceStatuses.exists(attendanceStatus)) {
                throw new ValueMustBeFromEnum("attendance_status", attendanceStatus, AttendanceStatuses.class);
            }

            int mark = parsePositiveInt(ctx, "mark");

            Mark existing = this.repository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Оценка/посещаемость", id));

            // проверки существования расписания и студента
            this.timetablesRepository.getById(timetableId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Расписание", timetableId));
            this.usersRepository.getById(studentId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Студент", studentId));

            Mark updatedMark = new Mark(id, timetableId, studentId, lessonRealDate,
                    LocalDateTime.now(), attendanceStatus, mark);

            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedMark);
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}