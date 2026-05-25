package ru.ayozav.database.repositories;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Semester;

import java.time.LocalDate;
import java.util.Optional;

@Testcontainers
public class SemestersEventRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer("postgres:18")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");


    private static HikariConnectionFactory factory;
    private SemestersEventRepository repository;

    @BeforeAll
    static void init() {
        // Настройка пула соединений на основе параметров контейнера
        factory = new HikariConnectionFactory(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword(),
                "public"
        );
        // Здесь нужно выполнить миграции (например, Flyway или выполнить schema.sql)
        runMigrations(factory);
    }

    @AfterAll
    static void close() {
        if (factory != null) factory.close();
    }

    @BeforeEach
    void setUp() {
        repository = new SemestersEventRepository(factory);
        cleanUpDatabase();
    }

    private void cleanUpDatabase() {

    }

    private static void runMigrations(HikariConnectionFactory factory) {
        DatabaseMigrator databaseMigrator = new DatabaseMigrator(factory);
        databaseMigrator.runMigrations();
    }

    @Test
    void testAddAndGetAndDeleteSemester() throws DatabaseException {
        // Когда
        int id = repository.add("Autumn 2025", LocalDate.of(2025, 9, 1), LocalDate.of(2026, 1, 31));

        // Тогда
        Optional<Semester> semester = repository.getById(id);
        Assertions.assertTrue(semester.isPresent());
        Assertions.assertEquals("Autumn 2025", semester.get().getName());
        Assertions.assertEquals(LocalDate.of(2025, 9, 1), semester.get().getStart());
        Assertions.assertEquals(LocalDate.of(2026, 1, 31), semester.get().getEnd());

        repository.deleteById(id);
        semester = repository.getById(id);
        Assertions.assertFalse(semester.isPresent());
    }
}
