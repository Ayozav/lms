package ru.ayozav.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ru.ayozav.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class UsersMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        int id = rs.getInt("id");
        UUID openID = UUID.fromString(rs.getString("open_id"));
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String patronymic = rs.getString("patronymic");
        LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
        return new User(id, openID, firstName, lastName, patronymic, birthDate);
    }
}
