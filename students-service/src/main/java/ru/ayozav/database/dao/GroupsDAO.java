package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.GroupMapper;
import ru.ayozav.models.Group;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(GroupMapper.class)
public interface GroupsDAO {

    @SqlUpdate(
            "INSERT INTO groups (group_name, headman_id, first_semester_id, course_level, grade_id) " +
                    "VALUES (:group_name, :headman_id, :first_semester_id, :course_level, :grade_id)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("group_name") String groupName,
            @Bind("headman_id") int headmanId,
            @Bind("first_semester_id") int firstSemesterId,
            @Bind("course_level") int courseLevel,
            @Bind("grade_id") int gradeId
    );

    @SqlQuery(
            "SELECT id, group_name, headman_id, first_semester_id, course_level, grade_id " +
                    "FROM groups " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Group> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, group_name, headman_id, first_semester_id, course_level, grade_id " +
                    "FROM groups " +
                    "WHERE id = :id"
    )
    Optional<Group> getById(@Bind("id") int id);

    @SqlUpdate(
            "DELETE FROM groups WHERE id = :id"
    )
    void deleteById(@Bind("id") int id);
}