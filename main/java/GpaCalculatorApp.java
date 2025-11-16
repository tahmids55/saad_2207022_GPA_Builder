package com.gpa.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

// This is the main entry point for the JavaFX application.
public class GpaCalculatorApp extends Application {

    private static final String HISTORY_FILE = "gpa_history.csv";

    // The start method is the main entry point for all JavaFX applications.
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load course history before loading the UI
            loadCourseHistory();

            // Load the FXML file for the home screen.
            Parent root = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));

            // Create a new scene with the loaded FXML content.
            Scene scene = new Scene(root);

            // Load the CSS stylesheet and apply it to the scene.
            String css = this.getClass().getResource("styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            // Set the title of the application window.
            primaryStage.setTitle("GPA Calculator");
            // Set the scene for the stage.
            primaryStage.setScene(scene);
            // The window is now resizable to accommodate the larger table
            primaryStage.setResizable(true);
            // Show the stage.
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Failed to load the initial screen.");
            e.printStackTrace();
        }
    }

    // The stop method is called when the application is closed.
    @Override
    public void stop() {
        try {
            // Save the course list to the history file.
            saveCourseHistory();
        } catch (IOException e) {
            System.err.println("Failed to save course history.");
            e.printStackTrace();
        }
    }

    // Loads the course history from the CSV file into the static course list.
    private void loadCourseHistory() {
        Path path = Paths.get(HISTORY_FILE);
        if (Files.exists(path)) {
            try {
                List<String> lines = Files.readAllLines(path);
                List<Course> courses = lines.stream()
                        .map(Course::fromCsvRow)
                        .collect(Collectors.toList());
                CourseEntryScreenController.courseList.setAll(courses);
            } catch (IOException e) {
                System.err.println("Error loading course history: " + e.getMessage());
            }
        }
    }

    // Saves the current course list to the CSV file.
    private void saveCourseHistory() throws IOException {
        List<String> lines = CourseEntryScreenController.courseList.stream()
                .map(Course::toCsvRow)
                .collect(Collectors.toList());
        Files.write(Paths.get(HISTORY_FILE), lines);
    }


    // The main method is needed for some IDEs to launch the JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}

