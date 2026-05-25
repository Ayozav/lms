package ru.ayozav.database;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

@Slf4j
public class DatabaseMigrator {
    private final HikariConnectionFactory connectionFactory;

    public DatabaseMigrator(HikariConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void runMigrations() {
        try (Connection connection = connectionFactory.getConnection()) {
            // Указываем путь к мастер-файлу миграций
            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(connection)
            );

            // Применяем миграции (можно задавать разный контекст в зависимости от стенда - не очень хорошая практика)
            liquibase.update();
            System.out.println("Миграции успешно применены!");
        } catch (Exception e) {
            log.error("[DATABASE MIGRATOR ERROR]: ", e);
            throw new RuntimeException("Ошибка при выполнении миграций", e);
        }
    }

}
