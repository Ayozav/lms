package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.HomeworkMapper;
import ru.ayozav.models.Homework;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RegisterRowMapper(HomeworkMapper.class)
public interface HomeworksDAO {

    @SqlUpdate(
            "INSERT INTO homeworks (lesson_id, semester_id, deadline, description, file_link) " +
                    "VALUES (:lesson_id, :semester_id, :deadline, :description, :file_link)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("lesson_id") int lessonId,
            @Bind("semester_id") int semesterId,
            @Bind("deadline") LocalDateTime deadline,
            @Bind("description") String description,
            @Bind("file_link") String fileLink
    );

    @SqlQuery(
            "SELECT id, lesson_id, semester_id, deadline, description, file_link " +
                    "FROM homeworks " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Homework> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, lesson_id, semester_id, deadline, description, file_link " +
                    "FROM homeworks WHERE id = :id"
    )
    Optional<Homework> getById(@Bind("id") int id);

    @SqlUpdate("DELETE FROM homeworks WHERE id = :id")
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE homeworks SET " +
                    "lesson_id = :lesson_id, " +
                    "semester_id = :semester_id, " +
                    "deadline = :deadline, " +
                    "description = :description, " +
                    "file_link = :file_link " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("lesson_id") int lessonId,
            @Bind("semester_id") int semesterId,
            @Bind("deadline") LocalDateTime deadline,
            @Bind("description") String description,
            @Bind("file_link") String fileLink
    );
}