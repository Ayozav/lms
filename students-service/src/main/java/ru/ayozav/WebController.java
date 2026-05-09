package ru.ayozav;

import io.javalin.Javalin;
import ru.ayozav.controllers.UsersController;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;

import java.util.Map;


public class WebController {

    private Javalin app;
    private HikariConnectionFactory factory;
    private UsersController usersController;

    public void initialize() {

        String jdbcUrl = System.getenv("DATABASE_URL")
                .replace("${DATABASE_PORT}", System.getenv("DATABASE_PORT"))
                .replace("${DATABASE_NAME}", System.getenv("DATABASE_NAME"))
                .replace("\"", "");  // Чёртовы кавычки портили весь запуск. :)


        this.factory = new HikariConnectionFactory(
                jdbcUrl,
                System.getenv("DATABASE_USERNAME"),
                System.getenv("DATABASE_PASSWORD"),
                System.getenv("DATABASE_SCHEMA")
        );

        DatabaseMigrator databaseMigrator = new DatabaseMigrator(this.factory);
        databaseMigrator.runMigrations();

        this.usersController = new UsersController(factory);

        this.app = Javalin.create(
                javalinConfig -> {
                    javalinConfig.routes.get("/echo", this.usersController::echo);

                    javalinConfig.routes.get("/v1/users", this.usersController::getUsers);
                    javalinConfig.routes.get("/v1/user", this.usersController::getUser);
                    javalinConfig.routes.post("/v1/user", this.usersController::addUser);
                    javalinConfig.routes.delete("/v1/user", this.usersController::deleteUser);
                }
        );
    }

    public void run(int port) {
        this.app.start("0.0.0.0", port);
    }
}
