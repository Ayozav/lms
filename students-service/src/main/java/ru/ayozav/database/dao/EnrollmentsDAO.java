package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.EnrollmentMapper;
import ru.ayozav.models.Enrollment;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(EnrollmentMapper.class)
public interface EnrollmentsDAO {

    @SqlUpdate(
            "INSERT INTO enrollments (student_id, group_id, start_semester_id, end_semester_id) " +
                    "VALUES (:student_id, :group_id, :start_semester_id, :end_semester_id)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("student_id") int studentId,
            @Bind("group_id") int groupId,
            @Bind("start_semester_id") int startSemesterId,
            @Bind("end_semester_id") Integer endSemesterId
    );

    @SqlQuery(
            "SELECT id, student_id, group_id, start_semester_id, end_semester_id " +
                    "FROM enrollments " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Enrollment> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, student_id, group_id, start_semester_id, end_semester_id " +
                    "FROM enrollments " +
                    "WHERE id = :id"
    )
    Optional<Enrollment> getById(@Bind("id") int id);

    @SqlUpdate(
            "DELETE FROM enrollments WHERE id = :id"
    )
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE enrollments SET " +
                    "student_id = :student_id, " +
                    "group_id = :group_id, " +
                    "start_semester_id = :start_semester_id, " +
                    "end_semester_id = :end_semester_id " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("student_id") int studentId,
            @Bind("group_id") int groupId,
            @Bind("start_semester_id") int startSemesterId,
            @Bind("end_semester_id") Integer endSemesterId
    );
}