package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Semester;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SemesterMapper implements RowMapper<Semester> {
    @Override
    public Semester map(ResultSet rs, StatementContext ctx) throws SQLException {
        int id = rs.getInt("id");
        String semesterName = rs.getString("semester_name");
        LocalDate start = rs.getDate("start").toLocalDate();
        LocalDate end = rs.getDate("end").toLocalDate();
        return new Semester(
                id, semesterName, start, end
        );
    }
}
