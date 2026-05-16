package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.LessonsDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Lesson;

import java.util.List;
import java.util.Optional;

public class LessonsEventRepository {

    private static final Logger log = LoggerFactory.getLogger(LessonsEventRepository.class);
    private final LessonsDAO dao;
    private final int LESSONS_PER_PAGE = 20;

    public LessonsEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(LessonsDAO.class);
    }

    public int add(Lesson lesson) throws DatabaseException {
        try {
            log.info("Trying to add lesson for discipline {} order {}", lesson.getDisciplineId(), lesson.getOrderedNumber());
            return dao.insert(
                    lesson.getDisciplineId(),
                    lesson.getOrderedNumber(),
                    lesson.getMainTheme(),
                    lesson.getDescription(),
                    lesson.getTeacherFileLink(),
                    lesson.getStudentsFileLink(),
                    lesson.getType(),
                    lesson.getFormat(),
                    lesson.getRecommendRoom()
            );
        } catch (Exception e) {
            log.warn("[FAILED to insert lesson] discipline {}: {}", lesson.getDisciplineId(), e.getMessage());
            throw new DatabaseException("Не удалось добавить занятие.");
        }
    }

    public List<Lesson> getList(int page) {
        log.info("Getting page {} of lessons", page);
        return dao.getPage(LESSONS_PER_PAGE, (page - 1) * LESSONS_PER_PAGE);
    }

    public Optional<Lesson> getById(int id) {
        log.info("Getting lesson by id={}", id);
        return dao.getById(id);
    }

    public void deleteById(int id) throws DatabaseException {
        try {
            log.info("Deleting lesson id={}", id);
            dao.deleteById(id);
        } catch (Exception e) {
            log.warn("[FAILED to delete lesson] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось удалить занятие.");
        }
    }

    public void update(Lesson lesson) throws DatabaseException {
        try {
            log.info("Updating lesson id={}", lesson.getId());
            int rows = dao.update(
                    lesson.getId(),
                    lesson.getDisciplineId(),
                    lesson.getOrderedNumber(),
                    lesson.getMainTheme(),
                    lesson.getDescription(),
                    lesson.getTeacherFileLink(),
                    lesson.getStudentsFileLink(),
                    lesson.getType(),
                    lesson.getFormat(),
                    lesson.getRecommendRoom()
            );
            if (rows == 0) {
                throw new DatabaseException("Занятие с id=" + lesson.getId() + " не найдено.");
            }
        } catch (Exception e) {
            log.warn("[FAILED to update lesson] id={}: {}", lesson.getId(), e.getMessage());
            throw new DatabaseException("Не удалось обновить занятие.");
        }
    }
}