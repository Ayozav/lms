package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.Discipline;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DisciplineMapper implements RowMapper<Discipline> {

    @Override
    public Discipline map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Discipline(
                rs.getInt("id"),
                rs.getString("discipline_name"),
                rs.getInt("supervisor_id"),
                rs.getString("description"),
                rs.getInt("semester_id"),
                rs.getInt("grade_id")
        );
    }
}