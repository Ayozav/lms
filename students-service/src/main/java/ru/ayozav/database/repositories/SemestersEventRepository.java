package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.SemestersDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Semester;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SemestersEventRepository {

    private static final Logger log = LoggerFactory.getLogger(SemestersEventRepository.class);
    private final SemestersDAO dao;
    private final int SEMESTERS_PER_PAGE = 20;

    public SemestersEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(SemestersDAO.class);
    }

    public int add(String name, LocalDate start, LocalDate end) throws DatabaseException {
        try {
            log.info("Trying to add new semester: {} (start={}, end={})", name, start, end);
            return this.dao.insert(name, start, end);
        } catch (Exception e) {
            log.warn("[FAILED to insert semester]: {} (name={}, start={}, end={})", e, name, start, end);
            throw new DatabaseException("Добавить семестр (name=" + name + ") не вышло.");
        }
    }

    public List<Semester> getList(int page) {
        log.info("Trying to get page of semesters, page={}", page);
        return this.dao.getPage(
                this.SEMESTERS_PER_PAGE,
                (page - 1) * this.SEMESTERS_PER_PAGE
        );
    }

    public Optional<Semester> getById(int id) {
        log.info("Trying to get semester by id={}", id);
        return this.dao.getById(id);
    }

    public void deleteById(int id) {
        log.info("Trying to delete semester id={}", id);
        this.dao.deleteById(id);
    }
}