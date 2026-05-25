package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AttendanceStatuses {
    @JsonProperty("Присутствовал") PRESENT("present"),
    @JsonProperty("Отсутствовал") ABSENT("absent"),
    @JsonProperty("Опоздал") POST_GRADUATE("late"),
    @JsonProperty("Отсутствовал по уважительной причине") EXCUSED("excused");

    private final String status;
    AttendanceStatuses(String status) { this.status = status; }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public static boolean exists(String status) {
        return Arrays.stream(AttendanceStatuses.values())
                .anyMatch(type -> type.getStatus().equals(status) ||
                        type.name().equals(status)
                );
        }

}
