package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
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
}