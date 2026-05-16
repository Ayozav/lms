package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getWeekParity() {
        return weekParity;
    }

    public void setWeekParity(int weekParity) {
        this.weekParity = weekParity;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}