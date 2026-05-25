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
}