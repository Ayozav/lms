package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TimetableGroup {
    private int timetableId;
    private int groupId;

    public TimetableGroup(int timetableId, int groupId) {
        this.timetableId = timetableId;
        this.groupId = groupId;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}