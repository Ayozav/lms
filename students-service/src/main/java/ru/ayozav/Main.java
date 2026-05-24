package ru.ayozav;


import ru.ayozav.kafka.consumers.UserConsumer;

public class Main {

    public static void main(String[] args) {
        JavalinServer server = new JavalinServer();
        UserConsumer userConsumer = new UserConsumer(JavalinServer.KAFKA_BOOTSTRAP_SERVER);

        try {
            // Deprecated реализация: да, работает, но оно слишком... Синхронное.
            // WebController webController = new WebController();
            // webController.initialize();
            // webController.run(4040);

            userConsumer.start();
            server.start();

        } catch (RuntimeException e) {
            server.stop();
            userConsumer.close();
            throw new RuntimeException(e);
        }

    }
}
