package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Mark;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MarkMapper implements RowMapper<Mark> {

    @Override
    public Mark map(ResultSet rs, StatementContext ctx) throws SQLException {
        int markValue = rs.getInt("mark");
        Integer mark = rs.wasNull() ? null : markValue;

        return new Mark(
                rs.getInt("id"),
                rs.getInt("timetable_id"),
                rs.getInt("student_id"),
                rs.getObject("lesson_real_date", LocalDate.class),
                rs.getObject("updated_at", LocalDateTime.class),
                rs.getString("attendance_status"),
                mark
        );
    }
}