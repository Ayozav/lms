package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Mark {
    private int id;
    private int timetableId;
    private int studentId;
    private LocalDate lessonRealDate;
    private LocalDateTime updatedAt;
    private String attendanceStatus;
    private Integer mark; // nullable, because some marks may be absent

    public Mark(int id, int timetableId, int studentId, LocalDate lessonRealDate,
                LocalDateTime updatedAt, String attendanceStatus, Integer mark) {
        this.id = id;
        this.timetableId = timetableId;
        this.studentId = studentId;
        this.lessonRealDate = lessonRealDate;
        this.updatedAt = updatedAt;
        this.attendanceStatus = attendanceStatus;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public LocalDate getLessonRealDate() {
        return lessonRealDate;
    }

    public void setLessonRealDate(LocalDate lessonRealDate) {
        this.lessonRealDate = lessonRealDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }
}
