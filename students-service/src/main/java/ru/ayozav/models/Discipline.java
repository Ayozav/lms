package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Discipline {
    private int id;
    private String disciplineName;
    private int supervisorId;
    private String description;
    private int semesterId;
    private int gradeId;

    public Discipline(int id, String disciplineName, int supervisorId, String description, int semesterId, int gradeId) {
        this.id = id;
        this.disciplineName = disciplineName;
        this.supervisorId = supervisorId;
        this.description = description;
        this.semesterId = semesterId;
        this.gradeId = gradeId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDisciplineName() { return disciplineName; }
    public void setDisciplineName(String disciplineName) { this.disciplineName = disciplineName; }

    public int getSupervisorId() { return supervisorId; }
    public void setSupervisorId(int supervisorId) { this.supervisorId = supervisorId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getSemesterId() { return semesterId; }
    public void setSemesterId(int semesterId) { this.semesterId = semesterId; }

    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }
}