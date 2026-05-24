package ru.ayozav;

import ru.ayozav.kafka.consumers.GradeConsumer;
import ru.ayozav.kafka.consumers.UserConsumer;

public class Main {

    public static void main(String[] args) {
        WebController oldWebController = new WebController();

        JavalinServer server = new JavalinServer();
        UserConsumer userConsumer = new UserConsumer(JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory());
        GradeConsumer gradeConsumer = new GradeConsumer(JavalinServer.KAFKA_BOOTSTRAP_SERVER, server.getFactory());

        try {
            // Deprecated реализация: да, работает, но оно слишком... Синхронное.
            oldWebController.initialize();
            oldWebController.run(4040);

            userConsumer.start();
            gradeConsumer.start();
            server.start();

        } catch (RuntimeException e) {
            server.stop();
            userConsumer.close();
            gradeConsumer.close();
            throw new RuntimeException(e);
        }

    }
}
