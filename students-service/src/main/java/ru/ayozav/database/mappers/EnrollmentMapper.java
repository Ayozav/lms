package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Enrollment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentMapper implements RowMapper<Enrollment> {

    @Override
    public Enrollment map(ResultSet rs, StatementContext ctx) throws SQLException {
        int endSemesterId = rs.getInt("end_semester_id");
        Integer endSemester = rs.wasNull() ? null : endSemesterId;

        return new Enrollment(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getInt("group_id"),
                rs.getInt("start_semester_id"),
                endSemester
        );
    }
}