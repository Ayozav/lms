package ru.ayozav.answers;

public class SuccessObjectInsertAnswer {
    private final String objectName;
    private final int id;

    public SuccessObjectInsertAnswer(String objectName, int id) {
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
