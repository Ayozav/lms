package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.DisciplineMapper;
import ru.ayozav.models.Discipline;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(DisciplineMapper.class)
public interface DisciplinesDAO {

    @SqlUpdate(
            "INSERT INTO disciplines (discipline_name, supervisor_id, description, semester_id, grade_id) " +
                    "VALUES (:discipline_name, :supervisor_id, :description, :semester_id, :grade_id)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("discipline_name") String disciplineName,
            @Bind("supervisor_id") int supervisorId,
            @Bind("description") String description,
            @Bind("semester_id") int semesterId,
            @Bind("grade_id") int gradeId
    );

    @SqlQuery(
            "SELECT id, discipline_name, supervisor_id, description, semester_id, grade_id " +
                    "FROM disciplines " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Discipline> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, discipline_name, supervisor_id, description, semester_id, grade_id " +
                    "FROM disciplines " +
                    "WHERE id = :id"
    )
    Optional<Discipline> getById(@Bind("id") int id);

    @SqlUpdate(
            "DELETE FROM disciplines WHERE id = :id"
    )
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE disciplines SET " +
                    "discipline_name = :discipline_name, " +
                    "supervisor_id = :supervisor_id, " +
                    "description = :description, " +
                    "semester_id = :semester_id, " +
                    "grade_id = :grade_id " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("discipline_name") String disciplineName,
            @Bind("supervisor_id") int supervisorId,
            @Bind("description") String description,
            @Bind("semester_id") int semesterId,
            @Bind("grade_id") int gradeId
    );

}