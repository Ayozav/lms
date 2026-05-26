package ru.ayozav;

import ru.ayozav.kafka.consumers.*;

public class Main {

    public static void main(String[] args) {

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
        HomeworkConsumer homeworkConsumer = new HomeworkConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );
        AttachedHomeworkConsumer attachedHomeworkConsumer = new AttachedHomeworkConsumer(
                JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory()
        );

        try {
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
            homeworkConsumer.start();
            attachedHomeworkConsumer.start();

            server.start();

        } catch (RuntimeException e) {
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
            homeworkConsumer.close();
            attachedHomeworkConsumer.close();

            throw new RuntimeException(e);
        }
    }
}
