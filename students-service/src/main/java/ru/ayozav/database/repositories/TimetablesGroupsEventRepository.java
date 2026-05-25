package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.TimetablesGroupsDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.TimetableGroupLink;

import java.util.List;

public class TimetablesGroupsEventRepository {

    private static final Logger log = LoggerFactory.getLogger(TimetablesGroupsEventRepository.class);
    private final TimetablesGroupsDAO dao;
    private final int LINKS_PER_PAGE = 20;

    public TimetablesGroupsEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(TimetablesGroupsDAO.class);
    }

    public boolean get(int timetableId, int groupId) {
        return !dao.get(timetableId, groupId).isEmpty();
    }

    public void addLink(int timetableId, int groupId) throws DatabaseException {
        try {
            log.info("Adding link: timetable {} <-> group {}", timetableId, groupId);
            dao.insert(timetableId, groupId);
        } catch (Exception e) {
            log.warn("[FAILED to add link] timetable={}, group={}: {}", timetableId, groupId, e.getMessage());
            throw new DatabaseException("Не удалось привязать группу к расписанию.");
        }
    }

    public void deleteLink(int timetableId, int groupId) throws DatabaseException {
        try {
            log.info("Deleting link: timetable {} <-> group {}", timetableId, groupId);
            dao.delete(timetableId, groupId);
        } catch (Exception e) {
            log.warn("[FAILED to delete link] timetable={}, group={}: {}", timetableId, groupId, e.getMessage());
            throw new DatabaseException("Не удалось удалить привязку группы к расписанию.");
        }
    }

    public List<TimetableGroupLink> getGroupsForTimetable(int timetableId) {
        log.info("Getting groups for timetable id={}", timetableId);
        return dao.getByTimetableId(timetableId);
    }

    public List<TimetableGroupLink> getTimetablesForGroup(int groupId) {
        log.info("Getting timetables for group id={}", groupId);
        return dao.getByGroupId(groupId);
    }

    public List<TimetableGroupLink> getPage(int page) {
        log.info("Getting page {} of timetable-group links", page);
        return dao.getPage(LINKS_PER_PAGE, (page - 1) * LINKS_PER_PAGE);
    }
}