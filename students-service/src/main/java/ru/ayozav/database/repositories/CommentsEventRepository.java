package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.CommentsDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CommentsEventRepository {

    private static final Logger log = LoggerFactory.getLogger(CommentsEventRepository.class);
    private final CommentsDAO dao;
    private final int COMMENTS_PER_PAGE = 20;

    public CommentsEventRepository(HikariConnectionFactory factory) {
        Jdbi jdbi = Jdbi.create(factory.getDataSource());
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(CommentsDAO.class);
    }

    public int add(int attachedHomeworkId, int fromId, LocalDateTime sendTime, String message) throws DatabaseException {
        try {
            log.info("Adding comment: attached_hw={}, from={}, msg length={}", attachedHomeworkId, fromId, message.length());
            return dao.insert(attachedHomeworkId, fromId, sendTime, message);
        } catch (Exception e) {
            log.warn("[FAILED insert comment] attached_hw={}, from={}: {}", attachedHomeworkId, fromId, e.getMessage());
            throw new DatabaseException("Не удалось добавить комментарий.");
        }
    }

    public List<Comment> getList(int page) {
        log.info("Getting page {} of comments", page);
        return dao.getPage(COMMENTS_PER_PAGE, (page - 1) * COMMENTS_PER_PAGE);
    }

    public Optional<Comment> getById(int id) {
        log.info("Getting comment by id={}", id);
        return dao.getById(id);
    }

    public void deleteById(int id) throws DatabaseException {
        try {
            log.info("Deleting comment id={}", id);
            dao.deleteById(id);
        } catch (Exception e) {
            log.warn("[FAILED delete comment] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось удалить комментарий.");
        }
    }

    public void update(int id, int attachedHomeworkId, int fromId, LocalDateTime sendTime, String message) throws DatabaseException {
        try {
            log.info("Updating comment id={}", id);
            int rows = dao.update(id, attachedHomeworkId, fromId, sendTime, message);
            if (rows == 0) {
                throw new DatabaseException("Комментарий с id=" + id + " не найден.");
            }
        } catch (Exception e) {
            log.warn("[FAILED update comment] id={}: {}", id, e.getMessage());
            throw new DatabaseException("Не удалось обновить комментарий.");
        }
    }
}