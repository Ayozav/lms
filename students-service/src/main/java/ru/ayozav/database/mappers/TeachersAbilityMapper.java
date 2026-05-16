package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.TeachersAbility;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeachersAbilityMapper implements RowMapper<TeachersAbility> {

    @Override
    public TeachersAbility map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new TeachersAbility(
                rs.getInt("teacher_id"),
                rs.getInt("discipline_id")
        );
    }
}
