package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CommentMapper implements RowMapper<Comment> {

    @Override
    public Comment map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Comment(
                rs.getInt("id"),
                rs.getInt("attached_homework_id"),
                rs.getInt("from_id"),
                rs.getObject("send_time", LocalDateTime.class),
                rs.getString("message")
        );
    }
}
