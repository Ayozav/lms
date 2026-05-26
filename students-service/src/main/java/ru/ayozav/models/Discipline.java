package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class Discipline {
    private int id;
    private String disciplineName;
    private int supervisorId;
    private String description;
    private int semesterId; // ЭТО РЕЧЬ ПРО ТО, В КАКОМ СЕМЕСТРЕ БУДЕТ ТОТ ИЛИ ИНОЙ ПРЕДМЕТ!
    private int gradeId;

    public Discipline(int id, String disciplineName, int supervisorId, String description, int semesterId, int gradeId) {
        this.id = id;
        this.disciplineName = disciplineName;
        this.supervisorId = supervisorId;
        this.description = description;
        this.semesterId = semesterId;
        this.gradeId = gradeId;
    }
}