package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.TeachersAbilitiesDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.TeachersAbility;

import java.util.List;

public class TeachersAbilitiesEventRepository {

    private static final Logger log = LoggerFactory.getLogger(TeachersAbilitiesEventRepository.class);
    private final TeachersAbilitiesDAO dao;
    private final int LINKS_PER_PAGE = 20;

    public TeachersAbilitiesEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(TeachersAbilitiesDAO.class);
    }

    public void addLink(int teacherId, int disciplineId) throws DatabaseException {
        try {
            log.info("Adding ability: teacher {} can teach discipline {}", teacherId, disciplineId);
            dao.insert(teacherId, disciplineId);
        } catch (Exception e) {
            log.warn("[FAILED to add ability] teacher={}, discipline={}: {}", teacherId, disciplineId, e.getMessage());
            throw new DatabaseException("Не удалось добавить способность преподавателя.");
        }
    }

    public boolean exists(int teacherId, int disciplineId) {
        return !dao.get(teacherId, disciplineId).isEmpty();
    }

    public void deleteLink(int teacherId, int disciplineId) throws DatabaseException {
        try {
            log.info("Deleting ability: teacher {} no longer teaches discipline {}", teacherId, disciplineId);
            dao.delete(teacherId, disciplineId);
        } catch (Exception e) {
            log.warn("[FAILED to delete ability] teacher={}, discipline={}: {}", teacherId, disciplineId, e.getMessage());
            throw new DatabaseException("Не удалось удалить способность преподавателя.");
        }
    }

    public List<TeachersAbility> getDisciplinesForTeacher(int teacherId) {
        log.info("Getting disciplines for teacher id={}", teacherId);
        return dao.getByTeacherId(teacherId);
    }

    public List<TeachersAbility> getTeachersForDiscipline(int disciplineId) {
        log.info("Getting teachers for discipline id={}", disciplineId);
        return dao.getByDisciplineId(disciplineId);
    }

    public List<TeachersAbility> getPage(int page) {
        log.info("Getting page {} of teacher-discipline abilities", page);
        return dao.getPage(LINKS_PER_PAGE, (page - 1) * LINKS_PER_PAGE);
    }
}