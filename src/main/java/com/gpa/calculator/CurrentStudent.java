package com.gpa.calculator;

public class CurrentStudent {
    private static Student student;

    public static void setStudent(Student s) {
        student = s;
    }

    public static Student getStudent() {
        return student;
    }

    public static void clear() {
        student = null;
    }
}
