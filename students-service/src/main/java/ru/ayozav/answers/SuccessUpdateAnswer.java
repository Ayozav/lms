package ru.ayozav.answers;

public class SuccessUpdateAnswer {
    private final String objectName;
    private final int id;

    public SuccessUpdateAnswer(String objectName, int id) {
        this.objectName = objectName;
        this.id = id;
    }

    public String getObjectName() {
        return objectName;
    }

    public int getId() {
        return id;
    }
}
