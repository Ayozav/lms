package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.EnrollmentsDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Enrollment;

import java.util.List;
import java.util.Optional;

public class EnrollmentsEventRepository {

    private static final Logger log = LogManager.getLogger(EnrollmentsEventRepository.class);
    private final EnrollmentsDAO dao;
    private final int ENROLLMENTS_PER_PAGE = 20;

    public EnrollmentsEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(EnrollmentsDAO.class);
    }

    public int add(int studentId, int groupId, int startSemesterId, Integer endSemesterId) throws DatabaseException {
        try {
            log.info("Trying to add enrollment: student={}, group={}, startSemester={}, endSemester={}",
                    studentId, groupId, startSemesterId, endSemesterId);
            return dao.insert(studentId, groupId, startSemesterId, endSemesterId);
        } catch (Exception e) {
            log.warn("[FAILED to insert enrollment] student={}, group={}: {}", studentId, groupId, e.getMessage());
            throw new DatabaseException("Добавить запись о зачислении не вышло.");
        }
    }

    public List<Enrollment> getList(int page) {
        log.info("Trying to get page of enrollments, page={}", page);
        return dao.getPage(ENROLLMENTS_PER_PAGE, (page - 1) * ENROLLMENTS_PER_PAGE);
    }

    public Optional<Enrollment> getById(int id) {
        log.info("Trying to get enrollment by id={}", id);
        return dao.getById(id);
    }

    public void deleteById(int id) {
        log.info("Trying to delete enrollment id={}", id);
        dao.deleteById(id);
    }

    public void update(int id, int studentId, int groupId, int startSemesterId, Integer endSemesterId) throws DatabaseException {
        try {
            log.info("Trying to update enrollment id={}: student={}, group={}, startSemester={}, endSemester={}",
                    id, studentId, groupId, startSemesterId, endSemesterId);
            int rows = dao.update(id, studentId, groupId, startSemesterId, endSemesterId);
            if (rows == 0) {
                throw new DatabaseException("Запись о зачислении с id=" + id + " не найдена.");
            }
        } catch (Exception e) {
            log.warn("[FAILED to update enrollment id={}]: {}", id, e.getMessage());
            throw new DatabaseException("Обновить запись о зачислении с id=" + id + " не вышло.");
        }
    }
}