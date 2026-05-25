package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.EnrollmentsEventRepository;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.EnrollmentProducer;
import ru.ayozav.models.Enrollment;


public class EnrollmentsController extends ControllerSkeleton {

    private final EnrollmentsEventRepository enrollmentsEventRepository;
    private final EnrollmentProducer producer;

    public EnrollmentsController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.enrollmentsEventRepository = new EnrollmentsEventRepository(factory);
        this.producer = new EnrollmentProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            int studentId = this.parsePositiveInt(ctx, "student_id");
            int groupId = this.parsePositiveInt(ctx, "group_id");
            int startSemesterId = this.parsePositiveInt(ctx, "start_semester_id");
            int endSemesterId = this.parsePositiveInt(ctx, "end_semester_id");

            Enrollment enrollment = new Enrollment(0, studentId, groupId, startSemesterId, endSemesterId);
            this.producer.produceAdd(String.valueOf(studentId), enrollment);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.enrollmentsEventRepository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Enrollment enrollment = this.enrollmentsEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Запись о зачислении", id));
            new ObjectResponse<>(ctx, enrollment);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Enrollment existing = this.enrollmentsEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Запись о зачислении", id));

            new OkResponse(ctx);
            this.producer.produceDelete(String.valueOf(existing.getId()), existing);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            int studentId = this.parsePositiveInt(ctx, "student_id");
            int groupId = this.parsePositiveInt(ctx, "group_id");
            int startSemesterId = this.parsePositiveInt(ctx, "start_semester_id");
            int endSemesterId = this.parsePositiveInt(ctx, "end_semester_id");

            Enrollment existing = this.enrollmentsEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Зачисление", id));

            Enrollment updatedDiscipline = new Enrollment(id, studentId, groupId, startSemesterId, endSemesterId);
            this.producer.produceUpdate(String.valueOf(existing.getId()), updatedDiscipline);
            new OkResponse(ctx);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}
