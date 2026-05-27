package ru.ayozav;

import io.javalin.Javalin;

import io.javalin.http.Context;
import kotlin.IgnorableReturnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;
import ru.ayozav.javalin.controllers.*;
import ru.ayozav.javalin.responses.OkResponse;


public class JavalinServer {

    public static final String KAFKA_BOOTSTRAP_SERVER = System.getenv("KAFKA_BOOTSTRAP_SERVERS");

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
        GradesController gradesController = new GradesController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        SemestersController semestersController = new SemestersController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        DisciplinesController disciplinesController = new DisciplinesController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        EnrollmentsController enrollmentsController = new EnrollmentsController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        GroupsController groupsController = new GroupsController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        TimetablesController timetablesController = new TimetablesController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        TimetableGroupLinkController timetableGroupLinkController = new TimetableGroupLinkController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        LessonsController lessonsController = new LessonsController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        TeachersAbilitiesController teachersAbilitiesController = new TeachersAbilitiesController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        MarksController marksController = new MarksController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        HomeworksController homeworksController = new HomeworksController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        AttachedHomeworksController attachedHomeworksController = new AttachedHomeworksController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);
        CommentsController commentsController = new CommentsController(this.factory, JavalinServer.KAFKA_BOOTSTRAP_SERVER);




        return Javalin.create(

                javalinConfig -> {
                    javalinConfig.routes.before(this::preHandler);

                    javalinConfig.routes.get(
                            "/echo", OkResponse::new
                    );

                    // CRUD операции с User
                    javalinConfig.routes.get("/v1/user", usersController::getById);
                    javalinConfig.routes.get("/v1/users", usersController::getPage);
                    javalinConfig.routes.post("/v1/user", usersController::add);
                    javalinConfig.routes.delete("/v1/user", usersController::delete);
                    javalinConfig.routes.put("/v1/user", usersController::update);
                    // CRUD операции с Grade (уровень подготовки)
                    javalinConfig.routes.get("/v1/grade", gradesController::getById);
                    javalinConfig.routes.get("/v1/grades", gradesController::getPage);
                    javalinConfig.routes.post("/v1/grade", gradesController::add);
                    javalinConfig.routes.delete("/v1/grade", gradesController::delete);
                    javalinConfig.routes.put("/v1/grade", gradesController::update);
                    // CRUD операции с Semester
                    javalinConfig.routes.get("/v1/semester", semestersController::getById);
                    javalinConfig.routes.get("/v1/semesters", semestersController::getPage);
                    javalinConfig.routes.post("/v1/semester", semestersController::add);
                    javalinConfig.routes.delete("/v1/semester", semestersController::delete);
                    javalinConfig.routes.put("/v1/semester", semestersController::update);
                    // CRUD операции с Discipline
                    javalinConfig.routes.get("/v1/discipline", disciplinesController::getById);
                    javalinConfig.routes.get("/v1/disciplines", disciplinesController::getPage);
                    javalinConfig.routes.post("/v1/discipline", disciplinesController::add);
                    javalinConfig.routes.delete("/v1/discipline", disciplinesController::delete);
                    javalinConfig.routes.put("/v1/discipline", disciplinesController::update);
                    // CRUD операции с Enrollment
                    javalinConfig.routes.get("/v1/enrollment", enrollmentsController::getById);
                    javalinConfig.routes.get("/v1/enrollments", enrollmentsController::getPage);
                    javalinConfig.routes.post("/v1/enrollment", enrollmentsController::add);
                    javalinConfig.routes.delete("/v1/enrollment", enrollmentsController::delete);
                    javalinConfig.routes.put("/v1/enrollment", enrollmentsController::update);
                    // CRUD операции с Group
                    javalinConfig.routes.get("/v1/group", groupsController::getById);
                    javalinConfig.routes.get("/v1/groups", groupsController::getPage);
                    javalinConfig.routes.post("/v1/group", groupsController::add);
                    javalinConfig.routes.delete("/v1/group", groupsController::delete);
                    javalinConfig.routes.put("/v1/group", groupsController::update);

                    // CRUD операции с Timetable И TimetableGroupLink!
                    javalinConfig.routes.get("/v1/timetable", timetablesController::getById);
                    javalinConfig.routes.get("/v1/timetable/group", timetablesController::getByGroup);
                    // Привязка к расписанию группы на месте
                    javalinConfig.routes.post("/v1/timetable/group", timetableGroupLinkController::addLink);
                    javalinConfig.routes.delete("/v1/timetable/group", timetableGroupLinkController::deleteLink);
                    // И другие CRUD с Timetable...
                    javalinConfig.routes.get("/v1/timetable/teacher", timetablesController::getByTeacher);
                    javalinConfig.routes.get("/v1/timetables", timetablesController::getPage); // Перечень вообще всеобщего расписания
                    javalinConfig.routes.post("/v1/timetable", timetablesController::add);
                    javalinConfig.routes.delete("/v1/timetable", timetablesController::delete);
                    javalinConfig.routes.put("/v1/timetable", timetablesController::update);

                    // CRUD операции с Lesson
                    javalinConfig.routes.get("/v1/lesson", lessonsController::getById);
                    javalinConfig.routes.get("/v1/lessons", lessonsController::getPage);
                    javalinConfig.routes.post("/v1/lesson", lessonsController::add);
                    javalinConfig.routes.delete("/v1/lesson", lessonsController::delete);
                    javalinConfig.routes.put("/v1/lesson", lessonsController::update);

                    // CRD операции с TeachersAbilities
                    javalinConfig.routes.get("/v1/teacher/ability", teachersAbilitiesController::getDisciplinesByTeacher);
                    javalinConfig.routes.post("/v1/teacher/ability", teachersAbilitiesController::addAbility);
                    javalinConfig.routes.delete("/v1/teacher/ability", teachersAbilitiesController::deleteAbility);

                    // CRUD операции с Marks
                    javalinConfig.routes.get("/v1/mark", marksController::getById);
                    javalinConfig.routes.get("/v1/marks/student", marksController::getByStudentID);
                    javalinConfig.routes.get("/v1/marks", marksController::getPage);
                    javalinConfig.routes.post("/v1/mark", marksController::add);
                    javalinConfig.routes.delete("/v1/mark", marksController::delete);
                    javalinConfig.routes.put("/v1/mark", marksController::update);

                    javalinConfig.routes.get("/v1/homework", homeworksController::getById);
                    javalinConfig.routes.get("/v1/homeworks", homeworksController::getPage);
                    javalinConfig.routes.post("/v1/homework", homeworksController::add);
                    javalinConfig.routes.delete("/v1/homework", homeworksController::delete);
                    javalinConfig.routes.put("/v1/homework", homeworksController::update);

                    javalinConfig.routes.post("/v1/homework/attach", attachedHomeworksController::add);
                    javalinConfig.routes.delete("/v1/homework/attach", attachedHomeworksController::delete);
                    javalinConfig.routes.put("/v1/homework/attach", attachedHomeworksController::update);
                    javalinConfig.routes.get("/v1/homework/attached", attachedHomeworksController::getById);
                    javalinConfig.routes.get("/v1/homeworks/attached/all", attachedHomeworksController::getPage);

                    javalinConfig.routes.post("/v1/comment", commentsController::add);
                    javalinConfig.routes.delete("/v1/comment", commentsController::delete);
                    javalinConfig.routes.get("/v1/comment", commentsController::getById);
                    javalinConfig.routes.get("/v1/comments", commentsController::getPage);
                    javalinConfig.routes.put("/v1/comment", commentsController::update);
                }
        );
    }
}
