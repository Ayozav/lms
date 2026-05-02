package ru.ayozav;


public class Main {

    public static void main(String[] args) {

        WebController webController = new WebController();

        webController.initialize();
        webController.run(4040);
    }
}
