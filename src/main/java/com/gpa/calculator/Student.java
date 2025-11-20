package com.gpa.calculator;

public class Student {
    private int id;
    private String rollNumber;
    private String name;
    private double gpa;

    public Student(int id, String rollNumber, String name, double gpa) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.name = name;
        this.gpa = gpa;
    }

    public Student(String rollNumber, String name) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.gpa = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }
}
