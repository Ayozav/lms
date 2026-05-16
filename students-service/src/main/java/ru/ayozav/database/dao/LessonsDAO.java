package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.LessonMapper;
import ru.ayozav.models.Lesson;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(LessonMapper.class)
public interface LessonsDAO {

    @SqlUpdate(
            "INSERT INTO lessons (discipline_id, ordered_number, main_theme, description, " +
                    "teacher_file_link, students_file_link, type, format, recommend_room) " +
                    "VALUES (:discipline_id, :ordered_number, :main_theme, :description, " +
                    ":teacher_file_link, :students_file_link, :type, :format, :recommend_room)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("discipline_id") int disciplineId,
            @Bind("ordered_number") int orderedNumber,
            @Bind("main_theme") String mainTheme,
            @Bind("description") String description,
            @Bind("teacher_file_link") String teacherFileLink,
            @Bind("students_file_link") String studentsFileLink,
            @Bind("type") String type,
            @Bind("format") String format,
            @Bind("recommend_room") String recommendRoom
    );

    @SqlQuery(
            "SELECT id, discipline_id, ordered_number, main_theme, description, " +
                    "teacher_file_link, students_file_link, type, format, recommend_room " +
                    "FROM lessons " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Lesson> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, discipline_id, ordered_number, main_theme, description, " +
                    "teacher_file_link, students_file_link, type, format, recommend_room " +
                    "FROM lessons WHERE id = :id"
    )
    Optional<Lesson> getById(@Bind("id") int id);

    @SqlUpdate("DELETE FROM lessons WHERE id = :id")
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE lessons SET " +
                    "discipline_id = :discipline_id, " +
                    "ordered_number = :ordered_number, " +
                    "main_theme = :main_theme, " +
                    "description = :description, " +
                    "teacher_file_link = :teacher_file_link, " +
                    "students_file_link = :students_file_link, " +
                    "type = :type, " +
                    "format = :format, " +
                    "recommend_room = :recommend_room " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("discipline_id") int disciplineId,
            @Bind("ordered_number") int orderedNumber,
            @Bind("main_theme") String mainTheme,
            @Bind("description") String description,
            @Bind("teacher_file_link") String teacherFileLink,
            @Bind("students_file_link") String studentsFileLink,
            @Bind("type") String type,
            @Bind("format") String format,
            @Bind("recommend_room") String recommendRoom
    );
}