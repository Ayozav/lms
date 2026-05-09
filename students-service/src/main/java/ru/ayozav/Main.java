package ru.ayozav;


import ru.ayozav.database.DatabaseMigrator;

public class Main {

    public static void main(String[] args) {

        WebController webController = new WebController();

        webController.initialize();
        webController.run(4040);
    }
}
