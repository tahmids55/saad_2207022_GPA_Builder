package com.gpa.calculator;

// This class serves as the model for a single course.
// It holds all the information related to a course, such as its name, code,
// credit hours, teachers, and the grade received.
public class Course {

    // Fields to store course details
    private String courseName;
    private String courseCode;
    private int courseCredit;
    private String teacher1Name;
    private String teacher2Name;
    private String grade;

    // Constructor to initialize a Course object
    public Course(String courseName, String courseCode, int courseCredit, String teacher1Name, String teacher2Name, String grade) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseCredit = courseCredit;
        this.teacher1Name = teacher1Name;
        this.teacher2Name = teacher2Name;
        this.grade = grade;
    }

    // Getter for courseName. Required by JavaFXs PropertyValueFactory.
    public String getCourseName() {
        return courseName;
    }

    // Getter for courseCode.
    public String getCourseCode() {
        return courseCode;
    }

    // Getter for courseCredit.
    public int getCourseCredit() {
        return courseCredit;
    }

    // Getter for teacher1Name.
    public String getTeacher1Name() {
        return teacher1Name;
    }

    // Getter for teacher2Name.
    public String getTeacher2Name() {
        return teacher2Name;
    }

    // Getter for grade.
    public String getGrade() {
        return grade;
    }

    // --- CSV Serialization/Deserialization ---

    // Converts the Course object to a CSV-formatted string row.
    public String toCsvRow() {
        // Using a pipe delimiter to avoid issues with commas in course names.
        return String.join("|",
                getCourseName(),
                getCourseCode(),
                String.valueOf(getCourseCredit()),
                getTeacher1Name(),
                getTeacher2Name(),
                getGrade());
    }

    // Creates a Course object from a CSV-formatted string row.
    public static Course fromCsvRow(String csvRow) {
        String[] fields = csvRow.split("\\|");
        if (fields.length != 6) {
            throw new IllegalArgumentException("Invalid CSV row for Course: " + csvRow);
        }
        return new Course(
                fields[0],
                fields[1],
                Integer.parseInt(fields[2]),
                fields[3],
                fields[4],
                fields[5]
        );
    }
}

