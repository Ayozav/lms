package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.TimetablesGroupsMapper;
import ru.ayozav.models.TimetableGroup;

import java.util.List;

@RegisterRowMapper(TimetablesGroupsMapper.class)
public interface TimetablesGroupsDAO {

    @SqlUpdate(
            "INSERT INTO timetables_groups (timetable_id, group_id) " +
                    "VALUES (:timetable_id, :group_id)"
    )
    void insert(@Bind("timetable_id") int timetableId, @Bind("group_id") int groupId);

    @SqlUpdate(
            "DELETE FROM timetables_groups " +
                    "WHERE timetable_id = :timetable_id AND group_id = :group_id"
    )
    void delete(@Bind("timetable_id") int timetableId, @Bind("group_id") int groupId);

    @SqlQuery(
            "SELECT timetable_id, group_id FROM timetables_groups " +
                    "WHERE timetable_id = :timetable_id"
    )
    List<TimetableGroup> getByTimetableId(@Bind("timetable_id") int timetableId);

    @SqlQuery(
            "SELECT timetable_id, group_id FROM timetables_groups " +
                    "WHERE group_id = :group_id"
    )
    List<TimetableGroup> getByGroupId(@Bind("group_id") int groupId);

    @SqlQuery(
            "SELECT timetable_id, group_id FROM timetables_groups " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<TimetableGroup> getPage(@Bind("limit") int limit, @Bind("offset") int offset);
}