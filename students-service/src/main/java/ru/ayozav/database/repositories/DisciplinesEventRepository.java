package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.DisciplinesDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Discipline;

import java.util.List;
import java.util.Optional;

public class DisciplinesEventRepository {

    private static final Logger log = LoggerFactory.getLogger(DisciplinesEventRepository.class);
    private final DisciplinesDAO dao;
    private final int DISCIPLINES_PER_PAGE = 20;

    public DisciplinesEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(DisciplinesDAO.class);
    }

    public int add(String disciplineName, int supervisorId, String description, int semesterId, int gradeId) throws DatabaseException {
        try {
            log.info("Trying to add new discipline: {} (supervisor={}, semester={}, grade={})",
                    disciplineName, supervisorId, semesterId, gradeId);
            return this.dao.insert(disciplineName, supervisorId, description, semesterId, gradeId);
        } catch (Exception e) {
            log.warn("[FAILED to insert discipline]: {} (name={})", e, disciplineName);
            throw new DatabaseException("Добавить дисциплину (name=" + disciplineName + ") не вышло.");
        }
    }

    public List<Discipline> getList(int page) {
        log.info("Trying to get page of disciplines, page={}", page);
        return this.dao.getPage(
                this.DISCIPLINES_PER_PAGE,
                (page - 1) * this.DISCIPLINES_PER_PAGE
        );
    }

    public Optional<Discipline> getById(int id) {
        log.info("Trying to get discipline by id={}", id);
        return this.dao.getById(id);
    }

    public void deleteById(int id) {
        log.info("Trying to delete discipline id={}", id);
        this.dao.deleteById(id);
    }

    public void update(int id, String disciplineName, int supervisorId, String description,
                       int semesterId, int gradeId) throws DatabaseException {
        try {
            log.info("Trying to update discipline id={}: name={}", id, disciplineName);
            int rows = dao.update(id, disciplineName, supervisorId, description, semesterId, gradeId);
            if (rows == 0) {
                throw new DatabaseException("Дисциплина с id=" + id + " не найдена.");
            }
        } catch (Exception e) {
            log.warn("[FAILED to update discipline id={}]: {}", id, e.getMessage());
            throw new DatabaseException("Обновить дисциплину с id=" + id + " не вышло.");
        }
    }
}