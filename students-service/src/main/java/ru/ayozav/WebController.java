package ru.ayozav;

import io.javalin.Javalin;
import ru.ayozav.answers.EchoAnswer;
import ru.ayozav.controllers.*;
import ru.ayozav.database.DatabaseMigrator;
import ru.ayozav.database.HikariConnectionFactory;

/**
 * Он работает, его можно использовать, но
 * он работает СИНХРОННО, что не очень хорошо.
 * <p>
 * Бэк может повиснуть на одной операции и не ожить после.
 * */
@Deprecated
public class WebController {

    private Javalin app;

    private UsersController usersController;
    private GradesController gradesController;
    private SemestersController semestersController;
    private DisciplinesController disciplinesController;
    private GroupsController groupsController;

    private TimetablesController timetablesController;
    private EnrollmentsController enrollmentsController;
    private TimetablesGroupsController timetablesGroupsController;
    private LessonsController lessonsController;
    private TeachersAbilitiesController teachersAbilitiesController;
    private MarksController marksController;
    private HomeworksController homeworksController;
    private AttachedHomeworksController attachedHomeworksController;
    private CommentsController commentsController;

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
        this.disciplinesController = new DisciplinesController(factory);
        this.groupsController = new GroupsController(factory);
        this.timetablesController = new TimetablesController(factory);
        this.enrollmentsController = new EnrollmentsController(factory);
        this.timetablesGroupsController = new TimetablesGroupsController(factory);
        this.lessonsController = new LessonsController(factory);

        this.teachersAbilitiesController = new TeachersAbilitiesController(factory);
        this.marksController = new MarksController(factory);
        this.homeworksController = new HomeworksController(factory);
        this.attachedHomeworksController = new AttachedHomeworksController(factory);
        this.commentsController = new CommentsController(factory);

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
                    javalinConfig.routes.put("/v1/user", this.usersController::updateUser);

                    javalinConfig.routes.post("/v1/grade", this.gradesController::addGrade);
                    javalinConfig.routes.delete("/v1/grade", this.gradesController::deleteGrade);
                    javalinConfig.routes.get("/v1/grade", this.gradesController::getGradeById);
                    javalinConfig.routes.get("/v1/grades", this.gradesController::getAllGrades);
                    javalinConfig.routes.put("/v1/grade", this.gradesController::updateGrade);

                    javalinConfig.routes.post("/v1/semester", this.semestersController::addSemester);
                    javalinConfig.routes.delete("/v1/semester", this.semestersController::deleteSemester);
                    javalinConfig.routes.get("/v1/semester", this.semestersController::getSemesterById);
                    javalinConfig.routes.get("/v1/semesters", this.semestersController::getAllSemesters);
                    javalinConfig.routes.put("/v1/semester", this.semestersController::updateSemester);

                    javalinConfig.routes.post("/v1/discipline", this.disciplinesController::addDiscipline);
                    javalinConfig.routes.delete("/v1/discipline", this.disciplinesController::deleteDiscipline);
                    javalinConfig.routes.get("/v1/discipline", this.disciplinesController::getDisciplineById);
                    javalinConfig.routes.get("/v1/disciplines", this.disciplinesController::getAllDisciplines);
                    javalinConfig.routes.put("/v1/discipline", this.disciplinesController::updateDiscipline);

                    javalinConfig.routes.post("/v1/group", this.groupsController::addGroup);
                    javalinConfig.routes.delete("/v1/group", this.groupsController::deleteGroup);
                    javalinConfig.routes.get("/v1/group", this.groupsController::getGroupById);
                    javalinConfig.routes.get("/v1/groups", this.groupsController::getAllGroups);
                    javalinConfig.routes.put("/v1/group", this.groupsController::updateGroup);

                    javalinConfig.routes.post("/v1/timetable", this.timetablesController::addTimetable);
                    javalinConfig.routes.delete("/v1/timetable", this.timetablesController::deleteTimetable);
                    javalinConfig.routes.get("/v1/timetable", this.timetablesController::getTimetableById);
                    javalinConfig.routes.get("/v1/timetables", this.timetablesController::getAllTimetables);
                    javalinConfig.routes.put("/v1/timetable", this.timetablesController::updateTimetable);

                    javalinConfig.routes.post("/v1/enrollment", this.enrollmentsController::addEnrollment);
                    javalinConfig.routes.delete("/v1/enrollment", this.enrollmentsController::deleteEnrollment);
                    javalinConfig.routes.get("/v1/enrollment", this.enrollmentsController::getEnrollmentById);
                    javalinConfig.routes.get("/v1/enrollments", this.enrollmentsController::getAllEnrollments);
                    javalinConfig.routes.put("/v1/enrollment", this.enrollmentsController::updateEnrollment);

                    javalinConfig.routes.post("/v1/timetable_group", this.timetablesGroupsController::addLink);
                    javalinConfig.routes.delete("/v1/timetable_group", this.timetablesGroupsController::deleteLink);
                    javalinConfig.routes.get("/v1/timetable_group/groups", this.timetablesGroupsController::getGroupsByTimetable);
                    javalinConfig.routes.get("/v1/timetable_group/timetables", this.timetablesGroupsController::getTimetablesByGroup);
                    javalinConfig.routes.get("/v1/timetable_groups", this.timetablesGroupsController::getAllLinks);

                    javalinConfig.routes.post("/v1/lesson", this.lessonsController::addLesson);
                    javalinConfig.routes.delete("/v1/lesson", this.lessonsController::deleteLesson);
                    javalinConfig.routes.get("/v1/lesson", this.lessonsController::getLessonById);
                    javalinConfig.routes.get("/v1/lessons", this.lessonsController::getAllLessons);
                    javalinConfig.routes.put("/v1/lesson", this.lessonsController::updateLesson);

                    javalinConfig.routes.post("/v1/teacher_ability", this.teachersAbilitiesController::addAbility);
                    javalinConfig.routes.delete("/v1/teacher_ability", this.teachersAbilitiesController::deleteAbility);
                    javalinConfig.routes.get("/v1/teacher_ability/disciplines", this.teachersAbilitiesController::getDisciplinesByTeacher);
                    javalinConfig.routes.get("/v1/teacher_ability/teachers", this.teachersAbilitiesController::getTeachersByDiscipline);
                    javalinConfig.routes.get("/v1/teacher_abilities", this.teachersAbilitiesController::getAllAbilities);

                    javalinConfig.routes.post("/v1/mark", this.marksController::addMark);
                    javalinConfig.routes.delete("/v1/mark", this.marksController::deleteMark);
                    javalinConfig.routes.get("/v1/mark", this.marksController::getMarkById);
                    javalinConfig.routes.get("/v1/marks", this.marksController::getAllMarks);
                    javalinConfig.routes.put("/v1/mark", this.marksController::updateMark);

                    javalinConfig.routes.post("/v1/homework", this.homeworksController::addHomework);
                    javalinConfig.routes.delete("/v1/homework", this.homeworksController::deleteHomework);
                    javalinConfig.routes.get("/v1/homework", this.homeworksController::getHomeworkById);
                    javalinConfig.routes.get("/v1/homeworks", this.homeworksController::getAllHomeworks);
                    javalinConfig.routes.put("/v1/homework", this.homeworksController::updateHomework);

                    javalinConfig.routes.post("/v1/attached_homework", this.attachedHomeworksController::addAttachedHomework);
                    javalinConfig.routes.delete("/v1/attached_homework", this.attachedHomeworksController::deleteAttachedHomework);
                    javalinConfig.routes.get("/v1/attached_homework", this.attachedHomeworksController::getAttachedHomeworkById);
                    javalinConfig.routes.get("/v1/attached_homeworks", this.attachedHomeworksController::getAllAttachedHomeworks);
                    javalinConfig.routes.put("/v1/attached_homework", this.attachedHomeworksController::updateAttachedHomework);

                    javalinConfig.routes.post("/v1/comment", this.commentsController::addComment);
                    javalinConfig.routes.delete("/v1/comment", this.commentsController::deleteComment);
                    javalinConfig.routes.get("/v1/comment", this.commentsController::getCommentById);
                    javalinConfig.routes.get("/v1/comments", this.commentsController::getAllComments);
                    javalinConfig.routes.put("/v1/comment", this.commentsController::updateComment);
                }
        );
    }

    public void run(int port) {
        this.app.start("0.0.0.0", port);
    }

    public void stop() {
        this.app.stop();
    }
}
