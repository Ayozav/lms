package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.GradesDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Grade;

import java.util.List;
import java.util.Optional;

public class GradesEventRepository {

    private static final Logger log = LogManager.getLogger(GradesEventRepository.class);
    private final GradesDAO dao;
    private final int PER_PAGE = 20;


    public GradesEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(GradesDAO.class);
    }

    public int add(String code, String gradeName, String gradeType, int supervisorID) throws DatabaseException {
        try {
            log.info(
                    "Trying to add new grade: {} (grade: {} {}, supervisor_id={})",
                    code, gradeName, gradeType, supervisorID
            );
            return this.dao.insert(code, gradeName, gradeType, supervisorID);
        } catch (Exception e) {
            log.warn(
                    "[FAILED to insert grade]: {} (grade: {}; {} {}, supervisor_id={})",
                    e, code, gradeName, gradeType, supervisorID);
            throw new DatabaseException("Добавить уровень подготовки (code=" + code + ") не вышло.");
        }
    }

    public List<Grade> getList(int page) {
        log.info("Trying to get page of grades, page={}", page);
        return this.dao.getPage(
                this.PER_PAGE,
                (page - 1) * this.PER_PAGE
        );
    }

    public Optional<Grade> getById(int id) {
        log.info("Trying to get grade by id={}", id);
        return this.dao.getById(id);
    }

    public void deleteById(int id)  {
        log.info("Trying to delete grade id={}", id);
        this.dao.deleteById(id);
    }

    public void update(int id, String code, String gradeName, String gradeType, int supervisorID) throws DatabaseException {
        try {
            log.info("Trying to update grade id={}: code={}", id, code);
            int rows = dao.update(id, code, gradeName, gradeType, supervisorID);
            if (rows == 0) {
                throw new DatabaseException("Уровень подготовки с id=" + id + " не найден.");
            }
        } catch (Exception e) {
            log.warn("[FAILED to update grade id={}]: {}", id, e.getMessage());
            throw new DatabaseException("Обновить уровень подготовки с id=" + id + " не вышло.");
        }
    }
}
