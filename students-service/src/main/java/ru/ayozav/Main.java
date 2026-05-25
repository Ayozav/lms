package ru.ayozav;

import ru.ayozav.kafka.consumers.*;

public class Main {

    public static void main(String[] args) {
        // WebController oldWebController = new WebController();

        JavalinServer server = new JavalinServer();
        UserConsumer userConsumer = new UserConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        GradeConsumer gradeConsumer = new GradeConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        SemesterConsumer semesterConsumer = new SemesterConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        DisciplineConsumer disciplineConsumer = new DisciplineConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        EnrollmentConsumer enrollmentConsumer = new EnrollmentConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        GroupConsumer groupConsumer = new GroupConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        TimetableConsumer timetableConsumer = new TimetableConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );

        TimetableGroupLinkConsumer timetableGroupLinkConsumer = new TimetableGroupLinkConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );

        LessonConsumer lessonConsumer = new LessonConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );

        TeachersAbilityConsumer teachersAbilityConsumer = new TeachersAbilityConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        MarkConsumer markConsumer = new MarkConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );

        try {
            // Deprecated реализация: да, работает, но оно слишком... Синхронное.
            // oldWebController.initialize();
            // oldWebController.run(4040);

            userConsumer.start();
            gradeConsumer.start();
            semesterConsumer.start();
            disciplineConsumer.start();
            enrollmentConsumer.start();
            groupConsumer.start();
            timetableConsumer.start();
            timetableGroupLinkConsumer.start();
            lessonConsumer.start();
            teachersAbilityConsumer.start();
            markConsumer.start();

            server.start();

        } catch (RuntimeException e) {
            // oldWebController.stop();
            server.stop();
            userConsumer.close();
            gradeConsumer.close();
            semesterConsumer.close();
            disciplineConsumer.close();
            enrollmentConsumer.close();
            gradeConsumer.close();
            timetableConsumer.close();
            timetableGroupLinkConsumer.close();
            lessonConsumer.close();
            teachersAbilityConsumer.close();
            markConsumer.close();

            throw new RuntimeException(e);
        }
    }
}
