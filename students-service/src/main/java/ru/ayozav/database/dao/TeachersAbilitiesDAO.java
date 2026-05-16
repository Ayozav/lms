package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.TeachersAbilityMapper;
import ru.ayozav.models.TeachersAbility;

import java.util.List;

@RegisterRowMapper(TeachersAbilityMapper.class)
public interface TeachersAbilitiesDAO {

    @SqlUpdate(
            "INSERT INTO teachers_abilities (teacher_id, discipline_id) " +
                    "VALUES (:teacher_id, :discipline_id)"
    )
    void insert(@Bind("teacher_id") int teacherId, @Bind("discipline_id") int disciplineId);

    @SqlUpdate(
            "DELETE FROM teachers_abilities " +
                    "WHERE teacher_id = :teacher_id AND discipline_id = :discipline_id"
    )
    void delete(@Bind("teacher_id") int teacherId, @Bind("discipline_id") int disciplineId);

    @SqlQuery(
            "SELECT teacher_id, discipline_id FROM teachers_abilities " +
                    "WHERE teacher_id = :teacher_id"
    )
    List<TeachersAbility> getByTeacherId(@Bind("teacher_id") int teacherId);

    @SqlQuery(
            "SELECT teacher_id, discipline_id FROM teachers_abilities " +
                    "WHERE discipline_id = :discipline_id"
    )
    List<TeachersAbility> getByDisciplineId(@Bind("discipline_id") int disciplineId);

    @SqlQuery(
            "SELECT teacher_id, discipline_id FROM teachers_abilities " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<TeachersAbility> getPage(@Bind("limit") int limit, @Bind("offset") int offset);
}