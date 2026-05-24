package ru.ayozav.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
public class Semester {
    private int id;
    private String name;
    private LocalDate start;
    private LocalDate end;

    public Semester(int id, String semesterName, LocalDate start, LocalDate end) {
        this.id = id;
        this.name = semesterName;
        this.start = start;
        this.end = end;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
