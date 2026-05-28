package ru.ayozav.database.repositories;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.Lesson;

import java.util.Optional;

@Testcontainers
public class LessonsEventRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testingLessons")
            .withUsername("test")
            .withPassword("test");

    private static HikariConnectionFactory factory;
    private LessonsEventRepository repository;

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
        if (factory != null) {
            factory.close();
        }
    }

    @BeforeEach
    void setUp() {
        repository = new LessonsEventRepository(factory);
        cleanDatabase();
    }

    private void cleanDatabase() {}  // оставлено пустым, как в образце

    @Test
    void testAddAndDeleteAndGetLesson() throws DatabaseException {
        // Создаём объект Lesson (id пока 0)
        Lesson lesson = new Lesson(0, 10, 1, "Технологии программирования", "5а: введение в тестирование",
                "teacher", "students", "лекция", "онлайн", "Telemost");

        int id = repository.add(lesson);

        Optional<Lesson> newLesson = repository.getById(id);
        Assertions.assertTrue(newLesson.isPresent());

        Assertions.assertEquals(10, newLesson.get().getDisciplineId());
        Assertions.assertEquals(1, newLesson.get().getOrderedNumber());
        Assertions.assertEquals("Технологии программирования", newLesson.get().getMainTheme());
        Assertions.assertEquals("5а: введение в тестирование", newLesson.get().getDescription());
        Assertions.assertEquals("teacher", newLesson.get().getTeacherFileLink());
        Assertions.assertEquals("students", newLesson.get().getStudentsFileLink());
        Assertions.assertEquals("лекция", newLesson.get().getType());
        Assertions.assertEquals("онлайн", newLesson.get().getFormat());
        Assertions.assertEquals("Telemost", newLesson.get().getRecommendRoom());

        repository.deleteById(id);
        newLesson = repository.getById(id);
        Assertions.assertFalse(newLesson.isPresent());
    }
}