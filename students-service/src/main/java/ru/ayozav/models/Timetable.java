package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class Timetable {
    private int id;
    private int semesterId;
    private int disciplineId;
    private int teacherId;
    private int dayOfWeek;
    private int weekParity;
    private String room;
    private LocalTime startTime;
    private LocalTime endTime;

    public Timetable(int id, int semesterId, int disciplineId, int teacherId,
                     int dayOfWeek, int weekParity, String room,
                     LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.semesterId = semesterId;
        this.disciplineId = disciplineId;
        this.teacherId = teacherId;
        this.dayOfWeek = dayOfWeek;
        this.weekParity = weekParity;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}