package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.GradeMapper;
import ru.ayozav.models.Grade;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(GradeMapper.class)
public interface GradesDAO {
    @SqlUpdate(
            "INSERT INTO grades " +
                    "(code, grade_name, grade_type, supervisor_id) " +
                    "VALUES " +
                    "(:code, :grade_name, :grade_type, :supervisor_id)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("code") String code,
            @Bind("grade_name") String gradeName,
            @Bind("grade_type") String gradeType,
            @Bind("supervisor_id") int supervisorID
    );

    @SqlQuery(
            "SELECT id, code, grade_name, grade_type, supervisor_id " +
                    "FROM grades " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Grade> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, code, grade_name, grade_type, supervisor_id " +
                    "FROM grades " +
                    "WHERE id = :id"
    )
    Optional<Grade> getById(@Bind("id") int id);

    @SqlUpdate(
            "DELETE FROM grades WHERE id = :id"
    )
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE grades SET " +
                    "code = :code, " +
                    "grade_name = :grade_name, " +
                    "grade_type = :grade_type, " +
                    "supervisor_id = :supervisor_id " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("code") String code,
            @Bind("grade_name") String gradeName,
            @Bind("grade_type") String gradeType,
            @Bind("supervisor_id") int supervisorID
    );
}
