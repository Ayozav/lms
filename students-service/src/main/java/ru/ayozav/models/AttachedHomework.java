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
public class AttachedHomework {
    private int id;
    private int homeworkId;
    private int studentId;
    private Integer mark;      // may be null until graded
    private LocalDateTime attachDate;

    public AttachedHomework(int id, int homeworkId, int studentId, Integer mark, LocalDateTime attachDate) {
        this.id = id;
        this.homeworkId = homeworkId;
        this.studentId = studentId;
        this.mark = mark;
        this.attachDate = attachDate;
    }
}
