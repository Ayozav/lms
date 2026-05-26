package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class Enrollment {
    private int id;
    private int studentId;
    private int groupId;
    private int startSemesterId;
    private Integer endSemesterId; // may be null (active)

    public Enrollment(int id, int studentId, int groupId, int startSemesterId, Integer endSemesterId) {
        this.id = id;
        this.studentId = studentId;
        this.groupId = groupId;
        this.startSemesterId = startSemesterId;
        this.endSemesterId = endSemesterId;
    }
}