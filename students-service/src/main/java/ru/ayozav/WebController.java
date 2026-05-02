package ru.ayozav;

import io.javalin.Javalin;
import ru.ayozav.controllers.GroupsController;


public class WebController {

    private Javalin app;
    private GroupsController groupsController;

    public void initialize() {

        this.groupsController = new GroupsController();

        this.app = Javalin.create(
                javalinConfig -> {
                    javalinConfig.routes.get("/echo", this.groupsController::echo);
                    javalinConfig.routes.get("/app", this.groupsController::app);
                }
        );
    }

    public void run(int port) {
        this.app.start("0.0.0.0", port);
    }
}
