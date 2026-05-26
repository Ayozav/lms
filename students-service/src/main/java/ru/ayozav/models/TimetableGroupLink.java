package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class TimetableGroupLink {
    private int timetableId;
    private int groupId;

    public TimetableGroupLink(int timetableId, int groupId) {
        this.timetableId = timetableId;
        this.groupId = groupId;
    }
}