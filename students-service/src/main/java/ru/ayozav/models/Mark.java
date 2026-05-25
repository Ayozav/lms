package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class Mark {
    private int id;
    private int timetableId;
    private int studentId;
    private LocalDate lessonRealDate;
    private LocalDateTime updatedAt;
    private String attendanceStatus;
    private int mark;

    public Mark(int id, int timetableId, int studentId, LocalDate lessonRealDate,
                LocalDateTime updatedAt, String attendanceStatus, int mark) {
        this.id = id;
        this.timetableId = timetableId;
        this.studentId = studentId;
        this.lessonRealDate = lessonRealDate;
        this.updatedAt = updatedAt;
        this.attendanceStatus = attendanceStatus;
        this.mark = mark;
    }
}
