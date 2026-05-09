package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum GradeType {
    @JsonProperty("Бакалавриат") BACHELOR("BACH"),
    @JsonProperty("Магистратура") MASTER("MAST"),
    @JsonProperty("Специалитет") SPECIALTY("SPEC"),
    @JsonProperty("Аспирантура") POST_GRADUATE("POST"),
    @JsonProperty("Докторантура") DOCTORAL("DOCT"),
    @JsonProperty("Ординатура") MEDICAL_RESIDENCE("MED");

    private final String code;

    GradeType(String code) { this.code = code; }

    @JsonValue
    public String getCode() { return code; }

    public static boolean exists(String grade) {
        return Arrays.stream(GradeType.values())
                .anyMatch(type -> type.getCode().equals(grade) ||
                        type.name().equals(grade)
                );
    }

    public static GradeType[] list() {
        return GradeType.values();
    }
}
