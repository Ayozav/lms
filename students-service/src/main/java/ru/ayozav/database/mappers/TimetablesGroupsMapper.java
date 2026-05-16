package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.TimetableGroup;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimetablesGroupsMapper implements RowMapper<TimetableGroup> {

    @Override
    public TimetableGroup map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new TimetableGroup(
                rs.getInt("timetable_id"),
                rs.getInt("group_id")
        );
    }
}