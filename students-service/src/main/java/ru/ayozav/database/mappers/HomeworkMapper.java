package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Homework;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class HomeworkMapper implements RowMapper<Homework> {

    @Override
    public Homework map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Homework(
                rs.getInt("id"),
                rs.getInt("lesson_id"),
                rs.getInt("semester_id"),
                rs.getObject("deadline", LocalDateTime.class),
                rs.getString("description"),
                rs.getString("file_link")
        );
    }
}