package ru.ayozav.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Group {
    private int id;
    private String name;
    private List<User> users;

    public Group() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getStudents() {
        return users;
    }

    public void setStudents(List<User> users) {
        this.users = users;
    }
}
