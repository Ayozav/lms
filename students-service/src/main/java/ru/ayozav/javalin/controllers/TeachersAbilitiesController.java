package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.DisciplinesEventRepository;
import ru.ayozav.database.repositories.TeachersAbilitiesEventRepository;
import ru.ayozav.database.repositories.UsersEventRepository;
import ru.ayozav.javalin.exceptions.GreatException;
import ru.ayozav.javalin.exceptions.LinkAlreadyExistsException;
import ru.ayozav.javalin.exceptions.ObjectNotFoundInDatabase;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.javalin.responses.PageResponse;
import ru.ayozav.kafka.producers.TeacherAbilityProducer;
import ru.ayozav.models.TeachersAbility;

import java.util.List;

public class TeachersAbilitiesController extends ControllerSkeleton {

    private final TeachersAbilitiesEventRepository repository;
    private final UsersEventRepository usersRepository;
    private final DisciplinesEventRepository disciplinesRepository;
    private final TeacherAbilityProducer producer;

    public TeachersAbilitiesController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.repository = new TeachersAbilitiesEventRepository(factory);
        this.usersRepository = new UsersEventRepository(factory);
        this.disciplinesRepository = new DisciplinesEventRepository(factory);
        this.producer = new TeacherAbilityProducer(kafka_bootstrap_server);
    }

    public void addAbility(Context ctx) {
        try {
            int teacherId = this.parsePositiveInt(ctx, "teacher_id");
            int disciplineId = this.parsePositiveInt(ctx, "discipline_id");

            // проверка существования преподавателя и дисциплины
            this.usersRepository.getById(teacherId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Преподаватель", teacherId));
            this.disciplinesRepository.getById(disciplineId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", disciplineId));

            // проверка, что связь ещё не существует (опционально, если репозиторий не проверяет)

            if (repository.exists(teacherId, disciplineId))
                throw new LinkAlreadyExistsException("Возможность преподавателя");

            String key = teacherId + "_" + disciplineId;
            this.producer.produceAdd(key, new TeachersAbility(teacherId, disciplineId));
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void deleteAbility(Context ctx) {
        try {
            int teacherId = this.parsePositiveInt(ctx, "teacher_id");
            int disciplineId = this.parsePositiveInt(ctx, "discipline_id");

            if (!repository.exists(teacherId, disciplineId))
                throw new ObjectNotFoundInDatabase("Возможность преподавателя", teacherId);

            String key = teacherId + "_" + disciplineId;
            this.producer.produceDelete(key, new TeachersAbility(teacherId, disciplineId));
            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getDisciplinesByTeacher(Context ctx) {
        try {
            int teacherId = this.parsePositiveInt(ctx, "teacher_id");

            this.usersRepository.getById(teacherId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Преподаватель", teacherId));

            List<TeachersAbility> abilities = this.repository.getDisciplinesForTeacher(teacherId);
            new PageResponse<>(ctx, abilities);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void getTeachersByDiscipline(Context ctx) {
        try {
            int disciplineId = this.parsePositiveInt(ctx, "discipline_id");

            this.disciplinesRepository.getById(disciplineId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Дисциплина", disciplineId));

            List<TeachersAbility> abilities = this.repository.getTeachersForDiscipline(disciplineId);
            new PageResponse<>(ctx, abilities);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }
}
