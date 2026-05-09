package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.GradesMapper;
import ru.ayozav.models.Grade;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(GradesMapper.class)
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
    List<Grade> getPageOfGrades(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, code, grade_name, grade_type, supervisor_id " +
                    "FROM grades " +
                    "WHERE id = :id"
    )
    Optional<Grade> getGradeById(@Bind("id") int id);

    @SqlUpdate(
            "DELETE FROM grades WHERE id = :id"
    )
    void deleteGradeById(@Bind("id") int id);
}
