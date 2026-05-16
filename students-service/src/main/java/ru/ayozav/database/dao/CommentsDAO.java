package ru.ayozav.database.dao;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.ayozav.database.mappers.CommentMapper;
import ru.ayozav.models.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RegisterRowMapper(CommentMapper.class)
public interface CommentsDAO {

    @SqlUpdate(
            "INSERT INTO comments (attached_homework_id, from_id, send_time, message) " +
                    "VALUES (:attached_homework_id, :from_id, :send_time, :message)"
    )
    @GetGeneratedKeys
    int insert(
            @Bind("attached_homework_id") int attachedHomeworkId,
            @Bind("from_id") int fromId,
            @Bind("send_time") LocalDateTime sendTime,
            @Bind("message") String message
    );

    @SqlQuery(
            "SELECT id, attached_homework_id, from_id, send_time, message " +
                    "FROM comments " +
                    "LIMIT :limit OFFSET :offset"
    )
    List<Comment> getPage(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery(
            "SELECT id, attached_homework_id, from_id, send_time, message " +
                    "FROM comments WHERE id = :id"
    )
    Optional<Comment> getById(@Bind("id") int id);

    @SqlUpdate("DELETE FROM comments WHERE id = :id")
    void deleteById(@Bind("id") int id);

    @SqlUpdate(
            "UPDATE comments SET " +
                    "attached_homework_id = :attached_homework_id, " +
                    "from_id = :from_id, " +
                    "send_time = :send_time, " +
                    "message = :message " +
                    "WHERE id = :id"
    )
    int update(
            @Bind("id") int id,
            @Bind("attached_homework_id") int attachedHomeworkId,
            @Bind("from_id") int fromId,
            @Bind("send_time") LocalDateTime sendTime,
            @Bind("message") String message
    );
}