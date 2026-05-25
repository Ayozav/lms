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
}
