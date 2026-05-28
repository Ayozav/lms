package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.HomeworksDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Homework;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class HomeworksEventRepository {

    private static final Logger log = LogManager.getLogger(HomeworksEventRepository.class);
    private final HomeworksDAO dao;
    private final int HOMEWORKS_PER_PAGE = 20;

    public HomeworksEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(HomeworksDAO.class);
    }

    public int add(int lessonId, int semesterId, LocalDateTime deadline,
                   String description, String fileLink) throws DatabaseException {
        try {
            log.info("Adding homework: lesson={}, semester={}, deadline={}", lessonId, semesterId, deadline);
            return dao.insert(lessonId, semesterId, deadline, description, fileLink);
        } catch (Exception e) {
            log.warn("[FAILED insert homework] lesson={}, semester={}: {}", lessonId, semesterId, e.getMessage());
            throw new DatabaseException("Не удалось добавить домашнее задание.");
        }
    }

    public List<Homework> getList(int page) {
        log.info("Getting page {} of homeworks", page);
        return dao.getPage(HOMEWORKS_PER_PAGE, (page - 1) * HOMEWORKS_PER_PAGE);
    }

    public Optional<Homework> getById(int id) {
        log.info("Getting homework by id={}", id);
        return dao.getById(id);
    }

    public void deleteById(int id) throws DatabaseException {
        try {
            log.info("Deleting homework id={}", id);
            dao.deleteById(id);
        } catch (Exception e) {
            log.warn("[FAILED delete homework] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось удалить домашнее задание.");
        }
    }

    public void update(int id, int lessonId, int semesterId, LocalDateTime deadline,
                       String description, String fileLink) throws DatabaseException {
        try {
            log.info("Updating homework id={}", id);
            int rows = dao.update(id, lessonId, semesterId, deadline, description, fileLink);
            if (rows == 0) {
                throw new DatabaseException("Домашнее задание с id=" + id + " не найдено.");
            }
        } catch (Exception e) {
            log.warn("[FAILED update homework] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось обновить домашнее задание.");
        }
    }
}