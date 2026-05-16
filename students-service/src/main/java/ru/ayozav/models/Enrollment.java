package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Enrollment {
    private int id;
    private int studentId;
    private int groupId;
    private int startSemesterId;
    private Integer endSemesterId; // may be null (active)

    public Enrollment(int id, int studentId, int groupId, int startSemesterId, Integer endSemesterId) {
        this.id = id;
        this.studentId = studentId;
        this.groupId = groupId;
        this.startSemesterId = startSemesterId;
        this.endSemesterId = endSemesterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getStartSemesterId() {
        return startSemesterId;
    }

    public void setStartSemesterId(int startSemesterId) {
        this.startSemesterId = startSemesterId;
    }

    public Integer getEndSemesterId() {
        return endSemesterId;
    }

    public void setEndSemesterId(Integer endSemesterId) {
        this.endSemesterId = endSemesterId;
    }
}