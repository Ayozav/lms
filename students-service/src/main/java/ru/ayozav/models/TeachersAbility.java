package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class TeachersAbility {
    private int teacherId;
    private int disciplineId;

    public TeachersAbility(int teacherId, int disciplineId) {
        this.teacherId = teacherId;
        this.disciplineId = disciplineId;
    }
}
