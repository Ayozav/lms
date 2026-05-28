package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.GroupsDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Group;

import java.util.List;
import java.util.Optional;

public class GroupsEventRepository {

    private static final Logger log = LogManager.getLogger(GroupsEventRepository.class);
    private final GroupsDAO dao;
    private final int GROUPS_PER_PAGE = 20;

    public GroupsEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(GroupsDAO.class);
    }

    public int add(String groupName, int headmanId, int firstSemesterId, int courseLevel, int gradeId) throws DatabaseException {
        try {
            log.info("Trying to add new group: {} (headman={}, firstSemester={}, course={}, grade={})",
                    groupName, headmanId, firstSemesterId, courseLevel, gradeId);
            return this.dao.insert(groupName, headmanId, firstSemesterId, courseLevel, gradeId);
        } catch (Exception e) {
            log.warn("[FAILED to insert group]: {} (name={})", e, groupName);
            throw new DatabaseException("Добавить группу (group_name=" + groupName + ") не вышло.");
        }
    }

    public List<Group> getList(int page) {
        log.info("Trying to get page of groups, page={}", page);
        return this.dao.getPage(
                this.GROUPS_PER_PAGE,
                (page - 1) * this.GROUPS_PER_PAGE
        );
    }

    public Optional<Group> getById(int id) {
        log.info("Trying to get group by id={}", id);
        return this.dao.getById(id);
    }

    public void deleteById(int id) {
        log.info("Trying to delete group id={}", id);
        this.dao.deleteById(id);
    }

    public void update(int id, String groupName, int headmanId, int firstSemesterId,
                       int courseLevel, int gradeId) throws DatabaseException {
        try {
            log.info("Trying to update group id={}: name={}", id, groupName);
            int rows = dao.update(id, groupName, headmanId, firstSemesterId, courseLevel, gradeId);
            if (rows == 0) {
                throw new DatabaseException("Группа с id=" + id + " не найдена.");
            }
        } catch (Exception e) {
            log.warn("[FAILED to update group id={}]: {}", id, e.getMessage());
            throw new DatabaseException("Обновить группу с id=" + id + " не вышло.");
        }
    }
}