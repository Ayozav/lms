package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.AttachedHomeworksDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.AttachedHomework;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AttachedHomeworksEventRepository {

    private static final Logger log = LogManager.getLogger(AttachedHomeworksEventRepository.class);
    private final AttachedHomeworksDAO dao;
    private final int ATTACHMENTS_PER_PAGE = 20;

    public AttachedHomeworksEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(AttachedHomeworksDAO.class);
    }

    public int add(int homeworkId, int studentId, Integer mark, LocalDateTime attachDate) throws DatabaseException {
        try {
            log.info("Adding attachment: homework={}, student={}, date={}", homeworkId, studentId, attachDate);
            return dao.insert(homeworkId, studentId, mark, attachDate);
        } catch (Exception e) {
            log.warn("[FAILED insert attached homework] homework={}, student={}: {}", homeworkId, studentId, e.getMessage());
            throw new DatabaseException("Не удалось добавить сданное домашнее задание.");
        }
    }

    public List<AttachedHomework> getList(int page) {
        log.info("Getting page {} of attached homeworks", page);
        return dao.getPage(ATTACHMENTS_PER_PAGE, (page - 1) * ATTACHMENTS_PER_PAGE);
    }

    public Optional<AttachedHomework> getById(int id) {
        log.info("Getting attached homework by id={}", id);
        return dao.getById(id);
    }

    public void deleteById(int id) throws DatabaseException {
        try {
            log.info("Deleting attached homework id={}", id);
            dao.deleteById(id);
        } catch (Exception e) {
            log.warn("[FAILED delete attached homework] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось удалить сданное домашнее задание.");
        }
    }

    public void update(int id, int homeworkId, int studentId, Integer mark, LocalDateTime attachDate) throws DatabaseException {
        try {
            log.info("Updating attached homework id={}", id);
            int rows = dao.update(id, homeworkId, studentId, mark, attachDate);
            if (rows == 0) {
                throw new DatabaseException("Сданное домашнее задание с id=" + id + " не найдено.");
            }
        } catch (Exception e) {
            log.warn("[FAILED update attached homework] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось обновить сданное домашнее задание.");
        }
    }
}