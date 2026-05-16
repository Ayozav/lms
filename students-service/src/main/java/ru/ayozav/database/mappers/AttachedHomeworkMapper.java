package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.AttachedHomework;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AttachedHomeworkMapper implements RowMapper<AttachedHomework> {

    @Override
    public AttachedHomework map(ResultSet rs, StatementContext ctx) throws SQLException {
        int markValue = rs.getInt("mark");
        Integer mark = rs.wasNull() ? null : markValue;

        return new AttachedHomework(
                rs.getInt("id"),
                rs.getInt("homework_id"),
                rs.getInt("student_id"),
                mark,
                rs.getObject("attach_date", LocalDateTime.class)
        );
    }
}