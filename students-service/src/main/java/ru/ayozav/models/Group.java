package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class Group {
    private int id;
    private String groupName;
    private int headmanId;
    private int firstSemesterId;
    private int courseLevel;
    private int gradeId;

    public Group(int id, String groupName, int headmanId, int firstSemesterId, int courseLevel, int gradeId) {
        this.id = id;
        this.groupName = groupName;
        this.headmanId = headmanId;
        this.firstSemesterId = firstSemesterId;
        this.courseLevel = courseLevel;
        this.gradeId = gradeId;
    }
}