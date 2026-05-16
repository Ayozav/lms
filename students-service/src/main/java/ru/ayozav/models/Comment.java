package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Comment {
    private int id;
    private int attachedHomeworkId;
    private int fromId;
    private LocalDateTime sendTime;
    private String message;

    public Comment(int id, int attachedHomeworkId, int fromId, LocalDateTime sendTime, String message) {
        this.id = id;
        this.attachedHomeworkId = attachedHomeworkId;
        this.fromId = fromId;
        this.sendTime = sendTime;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttachedHomeworkId() {
        return attachedHomeworkId;
    }

    public void setAttachedHomeworkId(int attachedHomeworkId) {
        this.attachedHomeworkId = attachedHomeworkId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}