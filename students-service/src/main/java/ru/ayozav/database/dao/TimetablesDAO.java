package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.TimetableMapper;
import ru.ayozav.models.Timetable;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RegisterRowMapper(TimetableMapper.class)
public interface TimetablesDAO {

    @SqlUpdate(
            "INSERT INTO timetables (semester_id, discipline_id, teacher_id, " +
                    "day_of_week, week_parity, room, start_time, end_time) " +
                    "VALUES (:semester_id, :discipline_id, :teacher_id, :day_of_week, " +
                    ":week_parity, :room, :start_time, :end_time)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("semester_id") int semesterId,
            @Bind("discipline_id") int disciplineId,
            @Bind("teacher_id") int teacherId,
            @Bind("day_of_week") int dayOfWeek,
            @Bind("week_parity") int weekParity,
            @Bind("room") String room,
            @Bind("start_time") LocalTime startTime,
            @Bind("end_time") LocalTime endTime
    );

    @SqlQuery(
            "SELECT id, semester_id, discipline_id, teacher_id, day_of_week, " +
                    "week_parity, room, start_time, end_time FROM timetables " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Timetable> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, semester_id, discipline_id, teacher_id, day_of_week, " +
                    "week_parity, room, start_time, end_time FROM timetables " +
                    "WHERE id = :id"
    )
    Optional<Timetable> getById(@Bind("id") int id);


    @SqlQuery("SELECT t.* FROM timetables t " +
            "JOIN groups g ON g.first_semester_id = t.semester_id " +
            "WHERE g.id = :groupId " +
            "LIMIT :limit OFFSET :offset")
    List<Timetable> getByGroup(@Bind("groupId") int groupId,
                               @Bind("limit") int limit,
                               @Bind("offset") int offset);

    @SqlQuery("SELECT * FROM timetables WHERE teacher_id = :teacherId " +
            "LIMIT :limit OFFSET :offset")
    List<Timetable> getByTeacher(@Bind("teacherId") int teacherId,
                                 @Bind("limit") int limit,
                                 @Bind("offset") int offset);

    @SqlUpdate(
            "DELETE FROM timetables WHERE id = :id"
    )
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE timetables SET " +
                    "semester_id = :semester_id, " +
                    "discipline_id = :discipline_id, " +
                    "teacher_id = :teacher_id, " +
                    "day_of_week = :day_of_week, " +
                    "week_parity = :week_parity, " +
                    "room = :room, " +
                    "start_time = :start_time, " +
                    "end_time = :end_time " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("semester_id") int semesterId,
            @Bind("discipline_id") int disciplineId,
            @Bind("teacher_id") int teacherId,
            @Bind("day_of_week") int dayOfWeek,
            @Bind("week_parity") int weekParity,
            @Bind("room") String room,
            @Bind("start_time") LocalTime startTime,
            @Bind("end_time") LocalTime endTime
    );
}