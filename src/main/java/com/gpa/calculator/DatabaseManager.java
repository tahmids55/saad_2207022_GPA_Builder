package com.gpa.calculator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:gpa_calculator.db";
    private static Connection connection;

    public DatabaseManager() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create students table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "roll_number TEXT UNIQUE NOT NULL," +
                    "name TEXT NOT NULL," +
                    "gpa REAL DEFAULT 0.0," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create courses table
            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER NOT NULL," +
                    "course_name TEXT NOT NULL," +
                    "course_code TEXT NOT NULL," +
                    "credit REAL NOT NULL," +
                    "teacher1_name TEXT NOT NULL," +
                    "teacher2_name TEXT," +
                    "grade TEXT NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE)");

            System.out.println("Database tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add or retrieve a student
    public Student getOrCreateStudent(String rollNumber, String name) {
        try {
            // Check if student exists
            String query = "SELECT id, gpa FROM students WHERE roll_number = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, rollNumber);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Student student = new Student(rs.getInt("id"), rollNumber, name, rs.getDouble("gpa"));
                        return student;
                    }
                }
            }

            // Student doesn't exist, create new one
            String insertQuery = "INSERT INTO students (roll_number, name) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, rollNumber);
                pstmt.setString(2, name);
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        return new Student(newId, rollNumber, name, 0.0);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting or creating student: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Add course for a student
    public boolean addCourse(int studentId, Course course) {
        try {
            String query = "INSERT INTO courses (student_id, course_name, course_code, credit, teacher1_name, teacher2_name, grade) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, studentId);
                pstmt.setString(2, course.getCourseName());
                pstmt.setString(3, course.getCourseCode());
                pstmt.setDouble(4, course.getCourseCredit());
                pstmt.setString(5, course.getTeacher1Name());
                pstmt.setString(6, course.getTeacher2Name());
                pstmt.setString(7, course.getGrade());
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get all courses for a student
    public List<Course> getStudentCourses(int studentId) {
        List<Course> courses = new ArrayList<>();
        try {
            String query = "SELECT course_name, course_code, credit, teacher1_name, teacher2_name, grade " +
                    "FROM courses WHERE student_id = ? ORDER BY created_at";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, studentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Course course = new Course(
                                rs.getString("course_name"),
                                rs.getString("course_code"),
                                rs.getDouble("credit"),
                                rs.getString("teacher1_name"),
                                rs.getString("teacher2_name"),
                                rs.getString("grade")
                        );
                        courses.add(course);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    // Update student GPA
    public boolean updateStudentGpa(int studentId, double gpa) {
        try {
            String query = "UPDATE students SET gpa = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setDouble(1, gpa);
                pstmt.setInt(2, studentId);
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating GPA: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Clear all courses for a student
    public boolean clearStudentCourses(int studentId) {
        try {
            String query = "DELETE FROM courses WHERE student_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, studentId);
                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error clearing courses: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get student by roll number
    public Student getStudentByRollNumber(String rollNumber) {
        try {
            String query = "SELECT id, name, gpa FROM students WHERE roll_number = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, rollNumber);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Student(rs.getInt("id"), rollNumber, rs.getString("name"), rs.getDouble("gpa"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Close database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get connection (for potential reuse)
    public static Connection getConnection() {
        return connection;
    }
}
