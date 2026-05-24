package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Grade;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GradeMapper implements RowMapper<Grade> {
    @Override
    public Grade map(ResultSet rs, StatementContext ctx) throws SQLException {
        int id = rs.getInt("id");
        String code = rs.getString("code");
        String gradeName = rs.getString("grade_name");
        int supervisorID = rs.getInt("supervisor_id");
        String grade_type = rs.getString("grade_type");

        return new Grade(id, code, gradeName, supervisorID, grade_type);
    }
}
