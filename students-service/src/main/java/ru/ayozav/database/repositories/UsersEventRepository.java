package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.UsersDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersEventRepository {

    private static final Logger log = LoggerFactory.getLogger(UsersEventRepository.class);
    private final UsersDAO dao;

    private final int USERS_PER_PAGE = 10;

    public UsersEventRepository(HikariConnectionFactory factory) {
            Jdbi jdbi = Jdbi.create(factory.getDataSource());
            jdbi.installPlugin(new SqlObjectPlugin());
            this.dao = jdbi.onDemand(UsersDAO.class);
    }

    public int addUser(UUID openID, String firstName, String lastName, String patronymic, LocalDate birthDate) throws DatabaseException {
        try {
            log.info("Trying to add new user: {} (name: {} {})", openID, firstName, lastName);
            return this.dao.insert(openID, firstName, lastName, patronymic, birthDate);
        } catch (Exception e) {
            log.warn("[FAILED to insert user]: {} (user: {}, {} {}", e, openID, firstName, lastName);
            throw new DatabaseException("Добавить пользователя (openID=" + openID + ") не вышло.");
        }
    }

    public List<User> getUsers(int page) {
        log.info("Trying to get page of users, page={}", page);
        return this.dao.getPageOfUsers(
                this.USERS_PER_PAGE,
                (page - 1) * this.USERS_PER_PAGE
        );
    }

    public Optional<User> getUserById(int id) {
        log.info("Trying to get user by id={}", id);
        return this.dao.getUserById(id);
    }

    public void deleteUserById(int id)  {
        log.info("Trying to delete user id={}", id);
        this.dao.deleteUserById(id);
    }

    public void update(int id, UUID openID, String firstName, String lastName,
                       String patronymic, LocalDate birthDate) throws DatabaseException {
        try {
            log.info("Trying to update user id={}: {}", id, firstName);
            int rows = dao.update(id, openID, firstName, lastName, patronymic, birthDate);
            if (rows == 0) {
                throw new DatabaseException("Пользователь с id=" + id + " не найден.");
            }
        } catch (Exception e) {
            log.warn("[FAILED to update user id={}]: {}", id, e.getMessage());
            throw new DatabaseException("Обновить пользователя с id=" + id + " не вышло.");
        }
    }
}
