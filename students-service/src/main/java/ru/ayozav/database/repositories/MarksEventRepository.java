package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.MarksDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Mark;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MarksEventRepository {

    private static final Logger log = LogManager.getLogger(MarksEventRepository.class);
    private final MarksDAO dao;
    public final int MARKS_PER_PAGE = 20;

    public MarksEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(MarksDAO.class);
    }

    public int add(int timetableId, int studentId, LocalDate lessonRealDate,
                   String attendanceStatus, Integer mark) throws DatabaseException {
        try {
            log.info("Adding mark: timetable={}, student={}, date={}, status={}, mark={}",
                    timetableId, studentId, lessonRealDate, attendanceStatus, mark);
            return dao.insert(timetableId, studentId, lessonRealDate, attendanceStatus, mark);
        } catch (Exception e) {
            log.warn("[FAILED insert mark] timetable={}, student={}: {}", timetableId, studentId, e.getMessage());
            throw new DatabaseException("Не удалось добавить оценку/посещаемость.");
        }
    }

    public List<Mark> getList(int page) {
        log.info("Getting page {} of marks", page);
        return dao.getPage(MARKS_PER_PAGE, (page - 1) * MARKS_PER_PAGE);
    }

    public Optional<Mark> getById(int id) {
        log.info("Getting mark by id={}", id);
        return dao.getById(id);
    }

    public List<Mark> getByStudent(int studentID, LocalDate start, LocalDate end) {
        log.info("Getting marks for student={}", studentID);
        return dao.getByStudentIdAndDateRange(studentID, start, end);
    }

    public void deleteById(int id) throws DatabaseException {
        try {
            log.info("Deleting mark id={}", id);
            dao.deleteById(id);
        } catch (Exception e) {
            log.warn("[FAILED delete mark] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось удалить оценку/посещаемость.");
        }
    }

    public void update(int id, int timetableId, int studentId, LocalDate lessonRealDate,
                       String attendanceStatus, Integer mark) throws DatabaseException {
        try {
            log.info("Updating mark id={}", id);
            int rows = dao.update(id, timetableId, studentId, lessonRealDate, attendanceStatus, mark);
            if (rows == 0) {
                throw new DatabaseException("Оценка/посещаемость с id=" + id + " не найдена.");
            }
        } catch (Exception e) {
            log.warn("[FAILED update mark] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось обновить оценку/посещаемость.");
        }
    }
}