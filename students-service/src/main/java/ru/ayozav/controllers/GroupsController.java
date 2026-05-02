package ru.ayozav.controllers;

import io.javalin.http.Context;
import ru.ayozav.models.Group;
import ru.ayozav.models.Student;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroupsController {
    public GroupsController() { }

    public void echo(Context ctx) {
        /// Example:
        // List<Student> students = new LinkedList<>();
        // students.add(new Student(
        //         1, "Test1", "Testov", "Testovich",
        //         LocalDate.parse("2005-09-01")
        // ));
        // students.add(new Student(
        //         2, "Test2", "Testov", "Testovich",
        //         LocalDate.parse("2005-09-01")
        // ));
        // students.add(new Student(
        //         3, "Test3", "Testov", "Testovich",
        //         LocalDate.parse("2005-09-01")
        // ));
        // Group group = new Group();
        // group.setId(1);
        // group.setName("IS1-B23");
        // group.setStudents(students);
        //
        // ctx.status(200).json(group);

        ctx.status(200);
    }
}
