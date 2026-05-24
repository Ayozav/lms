package ru.ayozav;

import io.javalin.Javalin;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.answers.EchoAnswer;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.javalin.controllers.UsersController;

public class JavalinServer {

    public static final String KAFKA_BOOTSTRAP_SERVER = "EXTERNAL://" + System.getenv("KAFKA_BOOTSTRAP_SERVER");

    private static final String DATABASE_PORT = System.getenv("DATABASE_PORT");
    private static final String DATABASE_NAME = System.getenv("DATABASE_NAME");

    private static final String JDBC_URL = System.getenv("DATABASE_URL")
            .replace("${DATABASE_PORT}", DATABASE_PORT)
            .replace("${DATABASE_NAME}", DATABASE_NAME)
            .replace("\"", "");  // Чёртовы кавычки портили весь запуск. :)

    private static final String DATABASE_USERNAME = System.getenv("DATABASE_USERNAME");
    private static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");
    private static final String DATABASE_SCHEMA = System.getenv("DATABASE_SCHEMA");
    private static final Logger log = LoggerFactory.getLogger(JavalinServer.class);


    private final HikariConnectionFactory factory;
    private final Javalin app;

    public JavalinServer() {

        this.factory = new HikariConnectionFactory(
                JDBC_URL,
                DATABASE_USERNAME,
                DATABASE_PASSWORD,
                DATABASE_SCHEMA
        );

        DatabaseMigrator databaseMigrator = new DatabaseMigrator(factory);
        databaseMigrator.runMigrations();

        this.app = createApp();

    }

    public void start() {
        app.start(4040);
    }
    public void stop() {
        app.stop();
    }

    public void preHandler(Context ctx) {
        log.info("Pre-Handling: checking auth, permissions");
    }

    public Javalin createApp() {

        UsersController usersController = new UsersController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);

        return Javalin.create(

                javalinConfig -> {
                    javalinConfig.routes.before(this::preHandler);

                    javalinConfig.routes.get(
                            "/echo", ctx -> {
                                ctx.status(200).json(
                                        new EchoAnswer()
                                );
                            });

                    // Прямые маппинги без switch
                    javalinConfig.routes.get("/v1/user", usersController::getUser);
                    javalinConfig.routes.post("/v1/user", usersController::addUser);
                    javalinConfig.routes.delete("/v1/user", usersController::deleteUser);
                    javalinConfig.routes.put("/v1/user", usersController::updateUser);
                }
        );
    }
}
