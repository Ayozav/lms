package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.SemesterMapper;
import ru.ayozav.models.Semester;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RegisterRowMapper(SemesterMapper.class)
public interface SemestersDAO {

    @SqlUpdate(
            "INSERT INTO semesters (semester_name, start, \"end\") " +
                    "VALUES (:name, :start, :end)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("name") String name,
            @Bind("start") LocalDate start,
            @Bind("end") LocalDate end
    );

    @SqlQuery(
            "SELECT id, semester_name, semesters.start, semesters.end " +
                    "FROM semesters " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Semester> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, semester_name, semesters.start, semesters.end " +
                    "FROM semesters " +
                    "WHERE id = :id"
    )
    Optional<Semester> getById(@Bind("id") int id);

    @SqlUpdate(
            "DELETE FROM semesters WHERE id = :id"
    )
    void deleteById(@Bind("id") int id);
}
