package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Lesson {
    private int id;
    private int disciplineId;
    private int orderedNumber;
    private String mainTheme;
    private String description;
    private String teacherFileLink;
    private String studentsFileLink;
    private String type;
    private String format;
    private String recommendRoom;

    public Lesson(int id, int disciplineId, int orderedNumber, String mainTheme, String description,
                  String teacherFileLink, String studentsFileLink, String type, String format, String recommendRoom) {
        this.id = id;
        this.disciplineId = disciplineId;
        this.orderedNumber = orderedNumber;
        this.mainTheme = mainTheme;
        this.description = description;
        this.teacherFileLink = teacherFileLink;
        this.studentsFileLink = studentsFileLink;
        this.type = type;
        this.format = format;
        this.recommendRoom = recommendRoom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public int getOrderedNumber() {
        return orderedNumber;
    }

    public void setOrderedNumber(int orderedNumber) {
        this.orderedNumber = orderedNumber;
    }

    public String getMainTheme() {
        return mainTheme;
    }

    public void setMainTheme(String mainTheme) {
        this.mainTheme = mainTheme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherFileLink() {
        return teacherFileLink;
    }

    public void setTeacherFileLink(String teacherFileLink) {
        this.teacherFileLink = teacherFileLink;
    }

    public String getStudentsFileLink() {
        return studentsFileLink;
    }

    public void setStudentsFileLink(String studentsFileLink) {
        this.studentsFileLink = studentsFileLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRecommendRoom() {
        return recommendRoom;
    }

    public void setRecommendRoom(String recommendRoom) {
        this.recommendRoom = recommendRoom;
    }
}