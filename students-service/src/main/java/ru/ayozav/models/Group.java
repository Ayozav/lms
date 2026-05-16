package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Group {
    private int id;
    private String groupName;
    private int headmanId;
    private int firstSemesterId;
    private int courseLevel;
    private int gradeId;

    public Group(int id, String groupName, int headmanId, int firstSemesterId, int courseLevel, int gradeId) {
        this.id = id;
        this.groupName = groupName;
        this.headmanId = headmanId;
        this.firstSemesterId = firstSemesterId;
        this.courseLevel = courseLevel;
        this.gradeId = gradeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getHeadmanId() {
        return headmanId;
    }

    public void setHeadmanId(int headmanId) {
        this.headmanId = headmanId;
    }

    public int getFirstSemesterId() {
        return firstSemesterId;
    }

    public void setFirstSemesterId(int firstSemesterId) {
        this.firstSemesterId = firstSemesterId;
    }

    public int getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(int courseLevel) {
        this.courseLevel = courseLevel;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }
}