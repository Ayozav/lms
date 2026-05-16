package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.TimetablesDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Timetable;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class TimetablesEventRepository {

    private static final Logger log = LoggerFactory.getLogger(TimetablesEventRepository.class);
    private final TimetablesDAO dao;
    private final int TIMETABLES_PER_PAGE = 20;

    public TimetablesEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(TimetablesDAO.class);
    }

    public int add(int semesterId, int disciplineId, int teacherId,
                   int dayOfWeek, int weekParity, String room,
                   LocalTime startTime, LocalTime endTime) throws DatabaseException {
        try {
            log.info("Trying to add timetable entry: semester={}, discipline={}, teacher={}, day={}, parity={}",
                    semesterId, disciplineId, teacherId, dayOfWeek, weekParity);
            return this.dao.insert(semesterId, disciplineId, teacherId, dayOfWeek, weekParity, room, startTime, endTime);
        } catch (Exception e) {
            log.warn("[FAILED to insert timetable]: {}", e.getMessage());
            throw new DatabaseException("Добавить запись расписания не вышло.");
        }
    }

    public List<Timetable> getList(int page) {
        log.info("Trying to get page of timetables, page={}", page);
        return this.dao.getPage(
                this.TIMETABLES_PER_PAGE,
                (page - 1) * this.TIMETABLES_PER_PAGE
        );
    }

    public Optional<Timetable> getById(int id) {
        log.info("Trying to get timetable by id={}", id);
        return this.dao.getById(id);
    }

    public void deleteById(int id) {
        log.info("Trying to delete timetable id={}", id);
        this.dao.deleteById(id);
    }

    public void update(int id, int semesterId, int disciplineId, int teacherId,
                       int dayOfWeek, int weekParity, String room,
                       LocalTime startTime, LocalTime endTime) throws DatabaseException {
        try {
            log.info("Trying to update timetable id={}: semester={}, discipline={}, teacher={}, day={}, parity={}",
                    id, semesterId, disciplineId, teacherId, dayOfWeek, weekParity);
            int rowsUpdated = this.dao.update(id, semesterId, disciplineId, teacherId,
                    dayOfWeek, weekParity, room, startTime, endTime);
            if (rowsUpdated == 0) {
                throw new DatabaseException("Расписание с id=" + id + " не найдено.");
            }
        } catch (Exception e) {
            log.warn("[FAILED to update timetable id={}]: {}", id, e.getMessage());
            throw new DatabaseException("Обновить запись расписания с id=" + id + " не вышло: " + e.getMessage());
        }
    }
}