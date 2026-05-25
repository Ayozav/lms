package ru.ayozav;

import io.javalin.Javalin;

import io.javalin.http.Context;
import kotlin.IgnorableReturnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.answers.EchoAnswer;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.javalin.controllers.*;


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

    /// Игнорируем, потому что lombok ведёт себя с ним как-то неправильно,
    /// поэтому данный метод мы написали ручками...
    @IgnorableReturnValue
    public HikariConnectionFactory getFactory() {
        return this.factory;
    }

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
        app.start(4050);
    }
    public void stop() {
        app.stop();
    }

    public void preHandler(Context ctx) {
        log.info("Pre-Handling: checking auth, permissions");
    }

    public Javalin createApp() {

        UsersController usersController = new UsersController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        GradesController gradesController = new GradesController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        SemestersController semestersController = new SemestersController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        DisciplinesController disciplinesController = new DisciplinesController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        EnrollmentsController enrollmentsController = new EnrollmentsController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);

        return Javalin.create(

                javalinConfig -> {
                    javalinConfig.routes.before(this::preHandler);

                    javalinConfig.routes.get(
                            "/echo", ctx -> ctx.status(200).json(new EchoAnswer())
                    );

                    javalinConfig.routes.get("/v1/user", usersController::getById);
                    javalinConfig.routes.get("/v1/users", usersController::getPage);
                    javalinConfig.routes.post("/v1/user", usersController::add);
                    javalinConfig.routes.delete("/v1/user", usersController::delete);
                    javalinConfig.routes.put("/v1/user", usersController::update);

                    javalinConfig.routes.get("/v1/grade", gradesController::getById);
                    javalinConfig.routes.get("/v1/grades", gradesController::getPage);
                    javalinConfig.routes.post("/v1/grade", gradesController::add);
                    javalinConfig.routes.delete("/v1/grade", gradesController::delete);
                    javalinConfig.routes.put("/v1/grade", gradesController::update);

                    javalinConfig.routes.get("/v1/semester", semestersController::getById);
                    javalinConfig.routes.get("/v1/semesters", semestersController::getPage);
                    javalinConfig.routes.post("/v1/semester", semestersController::add);
                    javalinConfig.routes.delete("/v1/semester", semestersController::delete);
                    javalinConfig.routes.put("/v1/semester", semestersController::update);

                    javalinConfig.routes.get("/v1/discipline", disciplinesController::getById);
                    javalinConfig.routes.get("/v1/disciplines", disciplinesController::getPage);
                    javalinConfig.routes.post("/v1/discipline", disciplinesController::add);
                    javalinConfig.routes.delete("/v1/discipline", disciplinesController::delete);
                    javalinConfig.routes.put("/v1/discipline", disciplinesController::update);

                    javalinConfig.routes.get("/v1/enrollment", enrollmentsController::getById);
                    javalinConfig.routes.get("/v1/enrollments", enrollmentsController::getPage);
                    javalinConfig.routes.post("/v1/enrollment", enrollmentsController::add);
                    javalinConfig.routes.delete("/v1/enrollment", enrollmentsController::delete);
                    javalinConfig.routes.put("/v1/enrollment", enrollmentsController::update);

                }
        );
    }
}
