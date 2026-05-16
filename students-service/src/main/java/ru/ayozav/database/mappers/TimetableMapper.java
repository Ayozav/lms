package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Timetable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimetableMapper implements RowMapper<Timetable> {

    @Override
    public Timetable map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Timetable(
                rs.getInt("id"),
                rs.getInt("semester_id"),
                rs.getInt("discipline_id"),
                rs.getInt("teacher_id"),
                rs.getInt("day_of_week"),
                rs.getInt("week_parity"),
                rs.getString("room"),
                rs.getTime("start_time") != null ? rs.getTime("start_time").toLocalTime() : null,
                rs.getTime("end_time") != null ? rs.getTime("end_time").toLocalTime() : null
        );
    }
}