package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public LocalDateTime getAttachDate() {
        return attachDate;
    }

    public void setAttachDate(LocalDateTime attachDate) {
        this.attachDate = attachDate;
    }
}
