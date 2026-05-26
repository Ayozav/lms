package ru.ayozav.database.repositories;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.database.exceptions.DatabaseException;
import ru.ayozav.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UsersEventRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static HikariConnectionFactory factory;
    private UsersEventRepository repository;

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

    @AfterAll
    static void close() {
        if (factory != null) factory.close();
    }

    @BeforeEach
    void setUp() {
        repository = new UsersEventRepository(factory);
        cleanUpDatabase();
    }

    private void cleanUpDatabase() {
        try (var conn = factory.getDataSource().getConnection();
             var stmt = conn.createStatement()) {
            // Reset the users table, restart identity (auto‑increment)
            stmt.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean database", e);
        }
    }

    private static void runMigrations(HikariConnectionFactory factory) {
        DatabaseMigrator migrator = new DatabaseMigrator(factory);
        migrator.runMigrations();
    }


    @Test
    void testAddAndGetById() throws DatabaseException {
        UUID openId = UUID.randomUUID();
        LocalDate birth = LocalDate.of(1990, 5, 15);

        int id = repository.add(openId, "John", "Doe", "Jr.", birth);

        Optional<User> userOpt = repository.getById(id);
        assertTrue(userOpt.isPresent());
        User user = userOpt.get();

        assertEquals(id, user.getId());
        assertEquals(openId, user.getOpenID());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("Jr.", user.getPatronymic());
        assertEquals(birth, user.getBirthDate());
    }

    @Test
    void testGetByOpenId() throws DatabaseException {
        UUID openId = UUID.randomUUID();
        int id = repository.add(openId, "Jane", "Smith", null, LocalDate.of(1985, 8, 22));

        Optional<User> byOpenId = repository.getByOpenId(openId);
        assertTrue(byOpenId.isPresent());
        assertEquals(id, byOpenId.get().getId());

        // Non‑existent openId
        Optional<User> notFound = repository.getByOpenId(UUID.randomUUID());
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testGetPage() throws DatabaseException {
        // Insert 15 users
        for (int i = 1; i <= 15; i++) {
            repository.add(UUID.randomUUID(), "Name" + i, "Surname" + i, null, LocalDate.now());
        }

        // Page 1 (first 10)
        List<User> page1 = repository.getPage(1);
        assertEquals(10, page1.size());
        assertEquals("Name1", page1.getFirst().getFirstName());

        // Page 2 (next 5)
        List<User> page2 = repository.getPage(2);
        assertEquals(5, page2.size());
        assertEquals("Name11", page2.getFirst().getFirstName());

        // Page 3 (out of range) – empty list
        List<User> page3 = repository.getPage(3);
        assertTrue(page3.isEmpty());
    }

    @Test
    void testUpdate() throws DatabaseException {
        UUID originalOpenId = UUID.randomUUID();
        int id = repository.add(originalOpenId, "Old", "User", "Patr", LocalDate.of(2000, 1, 1));

        UUID newOpenId = UUID.randomUUID();
        LocalDate newBirth = LocalDate.of(2001, 2, 2);

        repository.update(id, newOpenId, "New", "Name", "UpdatedPatr", newBirth);

        Optional<User> updated = repository.getById(id);
        assertTrue(updated.isPresent());
        User u = updated.get();
        assertEquals(newOpenId, u.getOpenID());
        assertEquals("New", u.getFirstName());
        assertEquals("Name", u.getLastName());
        assertEquals("UpdatedPatr", u.getPatronymic());
        assertEquals(newBirth, u.getBirthDate());
    }

    @Test
    void testUpdateNonExistentThrowsException() {
        assertThrows(DatabaseException.class, () ->
                repository.update(9999, UUID.randomUUID(), "None", "Existent", null, LocalDate.now())
        );
    }

    @Test
    void testDeleteById() throws DatabaseException {
        int id = repository.add(UUID.randomUUID(), "ToDelete", "User", null, LocalDate.now());

        // Ensure it exists
        assertTrue(repository.getById(id).isPresent());

        repository.deleteById(id);

        // Should be gone
        assertTrue(repository.getById(id).isEmpty());
    }

    @Test
    void testAddDuplicateOpenIdThrowsException() throws DatabaseException {
        UUID openId = UUID.randomUUID();
        repository.add(openId, "First", "User", null, LocalDate.now());

        // Second insert with same openId should fail
        assertThrows(DatabaseException.class, () ->
                repository.add(openId, "Second", "User", null, LocalDate.now())
        );
    }

    @Test
    void testGetByIdNonExistentReturnsEmpty() {
        Optional<User> user = repository.getById(-1);
        assertTrue(user.isEmpty());
    }

    @Test
    void testGetByOpenIdNonExistentReturnsEmpty() {
        Optional<User> user = repository.getByOpenId(UUID.randomUUID());
        assertTrue(user.isEmpty());
    }
}