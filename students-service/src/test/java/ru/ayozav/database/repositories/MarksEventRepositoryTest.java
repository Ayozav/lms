package ru.ayozav.database.repositories;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Mark;

import java.time.LocalDate;
import java.util.Optional;


@Testcontainers
public class MarksEventRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testingMarks")
            .withUsername("test")
            .withPassword("test");

    private static HikariConnectionFactory factory;
    private MarksEventRepository repository;

    @BeforeAll
    static void init() {
        factory = new HikariConnectionFactory(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                "public"
        );
        runMigrations(factory);
    }

    private static void runMigrations(HikariConnectionFactory factory) {
        DatabaseMigrator databaseMigrator = new DatabaseMigrator(factory);
        databaseMigrator.runMigrations();
    }

    @AfterAll
    static void close() {
        if (factory !=null) {
            factory.close();
        }
    }

    @BeforeEach
    void setUp() {
        repository = new MarksEventRepository(factory);
        cleanDatabase();
    }

    private void cleanDatabase() {}

    @Test
    void testAddAndDeleteAndGetMakr() throws DatabaseException {
        int id = repository.add(1, 1, LocalDate.of(2026,  5, 1), "passed", 5);

        Optional<Mark> currnet_mark = repository.getById(id);
        Assertions.assertTrue(currnet_mark.isPresent());

        Assertions.assertEquals(1, currnet_mark.get().getTimetableId());
        Assertions.assertEquals(1, currnet_mark.get().getStudentId());
//        Assertions.assertEquals(LocalDate.of(2026, 5, 1), currnet_mark.get().getAttendanceStatus()); //YA X3
        Assertions.assertEquals("passed", currnet_mark.get().getAttendanceStatus());
        Assertions.assertEquals(5, currnet_mark.get().getMark());

        repository.deleteById(id);
        currnet_mark = repository.getById(id);
        Assertions.assertFalse(currnet_mark.isPresent());
    }
}
