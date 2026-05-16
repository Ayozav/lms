package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.AttachedHomeworkMapper;
import ru.ayozav.models.AttachedHomework;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RegisterRowMapper(AttachedHomeworkMapper.class)
public interface AttachedHomeworksDAO {

    @SqlUpdate(
            "INSERT INTO attached_homeworks (homework_id, student_id, mark, attach_date) " +
                    "VALUES (:homework_id, :student_id, :mark, :attach_date)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("homework_id") int homeworkId,
            @Bind("student_id") int studentId,
            @Bind("mark") Integer mark,
            @Bind("attach_date") LocalDateTime attachDate
    );

    @SqlQuery(
            "SELECT id, homework_id, student_id, mark, attach_date " +
                    "FROM attached_homeworks " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<AttachedHomework> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, homework_id, student_id, mark, attach_date " +
                    "FROM attached_homeworks WHERE id = :id"
    )
    Optional<AttachedHomework> getById(@Bind("id") int id);

    @SqlUpdate("DELETE FROM attached_homeworks WHERE id = :id")
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE attached_homeworks SET " +
                    "homework_id = :homework_id, " +
                    "student_id = :student_id, " +
                    "mark = :mark, " +
                    "attach_date = :attach_date " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("homework_id") int homeworkId,
            @Bind("student_id") int studentId,
            @Bind("mark") Integer mark,
            @Bind("attach_date") LocalDateTime attachDate
    );
}
