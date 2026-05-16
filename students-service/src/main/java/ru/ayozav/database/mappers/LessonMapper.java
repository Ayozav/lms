package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Lesson;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LessonMapper implements RowMapper<Lesson> {

    @Override
    public Lesson map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Lesson(
                rs.getInt("id"),
                rs.getInt("discipline_id"),
                rs.getInt("ordered_number"),
                rs.getString("main_theme"),
                rs.getString("description"),
                rs.getString("teacher_file_link"),
                rs.getString("students_file_link"),
                rs.getString("type"),
                rs.getString("format"),
                rs.getString("recommend_room")
        );
    }
}