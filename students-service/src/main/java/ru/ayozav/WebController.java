package ru.ayozav;

import io.javalin.Javalin;
import ru.ayozav.answers.EchoAnswer;
import ru.ayozav.controllers.GradesController;
import ru.ayozav.controllers.SemestersController;
import ru.ayozav.controllers.UsersController;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;

public class WebController {

    private Javalin app;

    private UsersController usersController;
    private GradesController gradesController;
    private SemestersController semestersController;

    public void initialize() {

        String jdbcUrl = System.getenv("DATABASE_URL")
                .replace("${DATABASE_PORT}", System.getenv("DATABASE_PORT"))
                .replace("${DATABASE_NAME}", System.getenv("DATABASE_NAME"))
                .replace("\"", "");  // Чёртовы кавычки портили весь запуск. :)

        HikariConnectionFactory factory = new HikariConnectionFactory(
                jdbcUrl,
                System.getenv("DATABASE_USERNAME"),
                System.getenv("DATABASE_PASSWORD"),
                System.getenv("DATABASE_SCHEMA")
        );

        DatabaseMigrator databaseMigrator = new DatabaseMigrator(factory);
        databaseMigrator.runMigrations();

        this.usersController = new UsersController(factory);
        this.gradesController = new GradesController(factory);
        this.semestersController = new SemestersController(factory);

        this.app = Javalin.create(
                javalinConfig -> {
                    javalinConfig.routes.get("/echo", (ctx -> {
                        ctx.status(200).json(
                                new EchoAnswer()
                        );
                    }));

                    javalinConfig.routes.get("/v1/users", this.usersController::getUsers);
                    javalinConfig.routes.get("/v1/user", this.usersController::getUser);
                    javalinConfig.routes.post("/v1/user", this.usersController::addUser);
                    javalinConfig.routes.delete("/v1/user", this.usersController::deleteUser);

                    javalinConfig.routes.post("/v1/grade", this.gradesController::addGrade);
                    javalinConfig.routes.delete("/v1/grade", this.gradesController::deleteGrade);
                    javalinConfig.routes.get("/v1/grade", this.gradesController::getGradeById);
                    javalinConfig.routes.get("/v1/grades", this.gradesController::getAllGrades);

                    javalinConfig.routes.post("/v1/semester", this.semestersController::addSemester);
                    javalinConfig.routes.delete("/v1/semester", this.semestersController::deleteSemester);
                    javalinConfig.routes.get("/v1/semester", this.semestersController::getSemesterById);
                    javalinConfig.routes.get("/v1/semesters", this.semestersController::getAllSemesters);

                }
        );
    }

    public void run(int port) {
        this.app.start("0.0.0.0", port);
    }
}
