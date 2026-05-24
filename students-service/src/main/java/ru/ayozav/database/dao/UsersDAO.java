package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.UserMapper;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterRowMapper(UserMapper.class)
public interface UsersDAO {

    @SqlUpdate(
            "INSERT INTO users " +
            "(open_id, first_name, last_name, patronymic, birth_date) " +
            "VALUES " +
            "(:open_id, :first_name, :last_name, :patronymic, :birth_date)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("open_id") UUID openID,
            @Bind("first_name") String firstName,
            @Bind("last_name") String lastName,
            @Bind("patronymic") String patronymic,
            @Bind("birth_date") LocalDate birthDate
    );

    @SqlQuery(
            "SELECT id, open_id, first_name, last_name, patronymic, birth_date " +
            "FROM users " +
            "LIMIT :limit OFFSET :offset"
    )
    List<User> getPageOfUsers(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, open_id, first_name, last_name, patronymic, birth_date " +
                    "FROM users " +
                    "WHERE id = :id"
    )
    Optional<User> getUserById(@Bind("id") int id);

    @SqlQuery(
            "SELECT id, open_id, first_name, last_name, patronymic, birth_date " +
                    "FROM users " +
                    "WHERE open_id = :open_id"
    )
    Optional<User> getUserByOpenId(@Bind("open_id") UUID openID);

    @SqlUpdate(
            "DELETE FROM users WHERE id = :id"
    )
    void deleteUserById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE users SET " +
                    "open_id = :open_id, " +
                    "first_name = :first_name, " +
                    "last_name = :last_name, " +
                    "patronymic = :patronymic, " +
                    "birth_date = :birth_date " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("open_id") UUID openID,
            @Bind("first_name") String firstName,
            @Bind("last_name") String lastName,
            @Bind("patronymic") String patronymic,
            @Bind("birth_date") LocalDate birthDate
    );
}
