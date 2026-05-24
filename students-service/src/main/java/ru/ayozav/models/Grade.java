package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@NoArgsConstructor
@Builder
public class Grade {
    private int id;
    private String code;
    private String gradeName;
    private int supervisorID;
    private String gradeType;

    public Grade(
            @JsonProperty("id") int id,
            @JsonProperty("code") String code,
            @JsonProperty("grade_name") String gradeName,
            @JsonProperty("supervisor_id") int supervisorID,
            @JsonProperty("grade_type") String gradeType) {
        this.id = id;
        this.code = code;
        this.gradeName = gradeName;
        this.supervisorID = supervisorID;
        this.gradeType = gradeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getSupervisorID() {
        return supervisorID;
    }

    public void setSupervisorID(int supervisorID) {
        this.supervisorID = supervisorID;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }
}
