package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class Homework {
    private int id;
    private int lessonId;
    private int semesterId;
    private LocalDateTime deadline;
    private String description;
    private String fileLink;

    public Homework(int id, int lessonId, int semesterId, LocalDateTime deadline,
                    String description, String fileLink) {
        this.id = id;
        this.lessonId = lessonId;
        this.semesterId = semesterId;
        this.deadline = deadline;
        this.description = description;
        this.fileLink = fileLink;
    }
}