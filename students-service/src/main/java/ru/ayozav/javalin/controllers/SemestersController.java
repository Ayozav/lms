package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.SemestersEventRepository;
import ru.ayozav.javalin.exceptions.DateRangeException;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.ObjectResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.SemesterProducer;
import ru.ayozav.models.Semester;

import java.time.LocalDate;

public class SemestersController extends ControllerSkeleton {

    private final SemestersEventRepository semestersEventRepository;
    private final SemesterProducer producer;

    public SemestersController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.semestersEventRepository = new SemestersEventRepository(factory);
        this.producer = new SemesterProducer(kafka_bootstrap_server);
    }

    public void add(Context ctx) {
        try {
            String name = this.queryParam(ctx, "name");
            LocalDate start = this.parseDate("start", this.queryParam(ctx, "start"));
            LocalDate end = this.parseDate("end", this.queryParam(ctx, "end"));

            if (start.isAfter(end)) {
                throw new DateRangeException();
            }

            new OkResponse(ctx);
            producer.produceAdd(name, new Semester(0, name, start, end));

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getPage(Context ctx) {
        try {
            int page = this.parsePositiveInt(ctx, "page");
            new PageResponse<>(ctx, this.semestersEventRepository.getList(page));
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Semester semester = this.semestersEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", id));
            new ObjectResponse<>(ctx, semester);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            Semester semester = this.semestersEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", id));

            new OkResponse(ctx);
            this.producer.produceDelete(String.valueOf(semester.getId()), semester);

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = this.parsePositiveInt(ctx, "id");
            String name = this.queryParam(ctx, "name");
            LocalDate start = this.parseDate("start", this.queryParam(ctx, "start"));
            LocalDate end = this.parseDate("end", this.queryParam(ctx, "end"));

            if (start.isAfter(end)) {
                throw new DateRangeException();
            }

            Semester semester = this.semestersEventRepository.getById(id)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Семестр", id));

            new OkResponse(ctx);
            this.producer.produceUpdate(String.valueOf(semester.getId()), new Semester(id, name, start, end));

        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}