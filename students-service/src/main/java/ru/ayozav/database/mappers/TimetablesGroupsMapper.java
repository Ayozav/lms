package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.TimetableGroupLink;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimetablesGroupsMapper implements RowMapper<TimetableGroupLink> {

    @Override
    public TimetableGroupLink map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new TimetableGroupLink(
                rs.getInt("timetable_id"),
                rs.getInt("group_id")
        );
    }
}