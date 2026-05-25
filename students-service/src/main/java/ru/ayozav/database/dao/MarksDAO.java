package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.MarkMapper;
import ru.ayozav.models.Mark;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RegisterRowMapper(MarkMapper.class)
public interface MarksDAO {

    @SqlUpdate(
            "INSERT INTO marks (timetable_id, student_id, lesson_real_date, attendance_status, mark) " +
                    "VALUES (:timetable_id, :student_id, :lesson_real_date, :attendance_status, :mark)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("timetable_id") int timetableId,
            @Bind("student_id") int studentId,
            @Bind("lesson_real_date") LocalDate lessonRealDate,
            @Bind("attendance_status") String attendanceStatus,
            @Bind("mark") Integer mark
    );

    @SqlQuery(
            "SELECT id, timetable_id, student_id, lesson_real_date, updated_at, attendance_status, mark " +
                    "FROM marks " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Mark> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, timetable_id, student_id, lesson_real_date, updated_at, attendance_status, mark " +
                    "FROM marks WHERE id = :id"
    )
    Optional<Mark> getById(@Bind("id") int id);


    @SqlQuery(
            "SELECT id, timetable_id, student_id, lesson_real_date, updated_at, attendance_status, mark " +
                    "FROM marks " +
                    "WHERE student_id = :student_id AND lesson_real_date BETWEEN :start_date AND :end_date " +
                    "ORDER BY lesson_real_date"
    )
    List<Mark> getByStudentIdAndDateRange(
            @Bind("student_id") int studentId,
            @Bind("start_date") LocalDate startDate,
            @Bind("end_date") LocalDate endDate
    );

    @SqlUpdate("DELETE FROM marks WHERE id = :id")
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE marks SET " +
                    "timetable_id = :timetable_id, " +
                    "student_id = :student_id, " +
                    "lesson_real_date = :lesson_real_date, " +
                    "attendance_status = :attendance_status, " +
                    "mark = :mark " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("timetable_id") int timetableId,
            @Bind("student_id") int studentId,
            @Bind("lesson_real_date") LocalDate lessonRealDate,
            @Bind("attendance_status") String attendanceStatus,
            @Bind("mark") Integer mark
    );
}