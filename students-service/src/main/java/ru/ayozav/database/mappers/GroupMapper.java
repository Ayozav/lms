package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Group(
                rs.getInt("id"),
                rs.getString("group_name"),
                rs.getInt("headman_id"),
                rs.getInt("first_semester_id"),
                rs.getInt("course_level"),
                rs.getInt("grade_id")
        );
    }
}