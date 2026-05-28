package ru.ayozav.database.repositories;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.dao.UsersDAO;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersEventRepository {

    private static final Logger log = LogManager.getLogger(UsersEventRepository.class);
    private final UsersDAO dao;

    private final int PER_PAGE = 10;

    public UsersEventRepository(HikariConnectionFactory factory) {
            Jdbi jdbi = Jdbi.create(factory.getDataSource());
            jdbi.installPlugin(new SqlObjectPlugin());
            this.dao = jdbi.onDemand(UsersDAO.class);
    }

    public int add(UUID openID, String firstName, String lastName, String patronymic, LocalDate birthDate) throws DatabaseException {
        try {
            log.info("Trying to add new user: {} (name: {} {})", openID, firstName, lastName);
            return this.dao.insert(openID, firstName, lastName, patronymic, birthDate);
        } catch (Exception e) {
            log.warn("[FAILED to insert user]: {} (user: {}, {} {}", e, openID, firstName, lastName);
            throw new DatabaseException("Добавить пользователя (openID=" + openID + ") не вышло.");
        }
    }

    public List<User> getPage(int page) {
        log.info("Trying to get page of users, page={}", page);
        return this.dao.getPageOfUsers(
                this.PER_PAGE,
                (page - 1) * this.PER_PAGE
        );
    }

    public Optional<User> getById(int id) {
        log.info("Trying to get user by id={}", id);
        return this.dao.getUserById(id);
    }

    public Optional<User> getByOpenId(UUID openID) {
        log.info("Trying to get user by open_id={}", openID);
        return this.dao.getUserByOpenId(openID);
    }

    public void deleteById(int id)  {
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
