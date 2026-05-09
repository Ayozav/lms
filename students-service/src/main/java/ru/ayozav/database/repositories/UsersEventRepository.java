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
            return this.dao.insert(openID, firstName, lastName, patronymic, birthDate);
        } catch (Exception e) {
            throw new DatabaseException("Добавить пользователя (openID=" + openID + ") не вышло.");
        }
    }

    public List<User> getUsers(int page) {
        return this.dao.getPageOfUsers(
                this.USERS_PER_PAGE,
                (page - 1) * this.USERS_PER_PAGE
        );
    }

    public Optional<User> getUserById(int id) {
        return this.dao.getUserById(id);
    }

    public void deleteUserById(int id)  {
        this.dao.deleteUserById(id);
    }
}
