package ru.ayozav.database.repositories;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.AttachedHomework;
import ru.ayozav.models.Homework;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class AttachedHomeworkEventRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testdb")
            .withPassword("test");

    private static HikariConnectionFactory factory;
    private AttachedHomeworksEventRepository repository;

    @BeforeAll
    static void init() {
        // коннекшн пул на основе контейнера
        factory = new HikariConnectionFactory(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                "public"
        );
        runMigrations(factory);
    }

    @AfterAll
    static void close() {
        if (factory != null) {
            factory.close();
        }
    }

    public static void runMigrations(HikariConnectionFactory factory) {
        DatabaseMigrator databaseMigrator = new DatabaseMigrator(factory);
        databaseMigrator.runMigrations();
    }

    @BeforeEach
//    @BeforeEach
    void setUp() {
        repository = new AttachedHomeworksEventRepository(factory);
        cleanUpDatabase();
    }

    private void cleanUpDatabase() {}

    @Test
    void testAttachAndDeleteAndGetHomework() throws DatabaseException {
        int id = repository.add(1, 1, 5, LocalDateTime.of(2023, 1, 1, 12, 0, 0));

        Optional<AttachedHomework> homework = repository.getById(id);
        Assertions.assertTrue(homework.isPresent());

        Assertions.assertEquals(1, homework.get().getHomeworkId());
        Assertions.assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0, 0), homework.get().getAttachDate());
        Assertions.assertEquals(1, homework.get().getStudentId());
        Assertions.assertEquals(5, homework.get().getMark());

        repository.deleteById(id);
        homework = repository.getById(id);
        Assertions.assertFalse(homework.isPresent());
    }
}
