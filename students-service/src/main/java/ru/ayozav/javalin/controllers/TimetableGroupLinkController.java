package ru.ayozav.javalin.controllers;

import io.javalin.http.Context;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.repositories.GroupsEventRepository;
import ru.ayozav.database.repositories.TimetablesEventRepository;
import ru.ayozav.database.repositories.TimetablesGroupsEventRepository;
import ru.ayozav.javalin.exceptions.*;
import ru.ayozav.javalin.responses.ErrorResponse;
import ru.ayozav.javalin.responses.OkResponse;
import ru.ayozav.kafka.producers.TimetableGroupLinkProducer;
import ru.ayozav.models.TimetableGroupLink;


public class TimetableGroupLinkController extends ControllerSkeleton {

    private final TimetablesGroupsEventRepository repository;
    private final TimetablesEventRepository timetablesRepository;
    private final GroupsEventRepository groupsRepository;
    private final TimetableGroupLinkProducer producer;

    public TimetableGroupLinkController(HikariConnectionFactory factory, String kafka_bootstrap_server) {
        this.repository = new TimetablesGroupsEventRepository(factory);
        this.timetablesRepository = new TimetablesEventRepository(factory);
        this.groupsRepository = new GroupsEventRepository(factory);
        this.producer = new TimetableGroupLinkProducer(kafka_bootstrap_server);
    }

    public void addLink(Context ctx) {
        try {
            int timetableId = this.parsePositiveInt(ctx, "timetable_id");
            int groupId = this.parsePositiveInt(ctx, "group_id");

            // проверка существования расписания и группы
            this.timetablesRepository.getById(timetableId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Расписание", timetableId));
            this.groupsRepository.getById(groupId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Группа", groupId));

            // проверка, не существует ли уже такая связь
            if (this.repository.get(timetableId, groupId)) {
                throw new ObjectNotFoundInDatabase("Связь с расписанием группы (group_id=" + groupId + ")",
                        timetableId
                );
            }

            // отправка события в Kafka
            String key = timetableId + "_" + groupId;
            this.producer.produceAdd(key, new TimetableGroupLink(timetableId, groupId));

            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

    public void deleteLink(Context ctx) {
        try {
            int timetableId = this.parsePositiveInt(ctx, "timetable_id");
            int groupId = this.parsePositiveInt(ctx, "group_id");

            // проверка существования расписания и группы (опционально, но для единообразия)
            this.timetablesRepository.getById(timetableId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Расписание", timetableId));
            this.groupsRepository.getById(groupId)
                    .orElseThrow(() -> new ObjectNotFoundInDatabase("Группа", groupId));

            // проверка, что связь существует
            if (!this.repository.get(timetableId, groupId)) {
                throw new ObjectNotFoundInDatabase("Связь с расписанием группы (group_id=" + groupId + ")",
                        timetableId
                );
            }

            // отправка события в Kafka
            String key = timetableId + "_" + groupId;
            this.producer.produceDelete(key, new TimetableGroupLink(timetableId, groupId));

            new OkResponse(ctx);
        } catch (GreatException exc) {
            new ErrorResponse(ctx, exc.getCode(), exc.getMessage());
        }
    }

}