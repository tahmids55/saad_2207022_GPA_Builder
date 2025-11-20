package com.gpa.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreenController {

    @FXML
    private TextField rollNumberField;
    @FXML
    private TextField nameField;
    @FXML
    private Label errorLabel;

    private DatabaseManager databaseManager;

    @FXML
    public void initialize() {
        databaseManager = new DatabaseManager();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String rollNumber = rollNumberField.getText().trim();
        String name = nameField.getText().trim();

        if (rollNumber.isEmpty() || name.isEmpty()) {
            errorLabel.setText("Error: Please enter both roll number and name.");
            return;
        }

        // Get or create student
        Student student = databaseManager.getOrCreateStudent(rollNumber, name);

        if (student != null) {
            // Store the current student in a static variable for use in other controllers
            CurrentStudent.setStudent(student);
            errorLabel.setText(""); // Clear error

            try {
                // Load the course entry screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseEntryScreen.fxml"));
                Parent courseEntryRoot = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(courseEntryRoot);
                stage.setScene(scene);
                stage.setTitle("Enter Course Details - " + name);
                stage.show();

            } catch (IOException e) {
                System.err.println("Failed to load the Course Entry screen.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Error: Failed to create or retrieve student.");
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            Parent homeScreenRoot = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(homeScreenRoot);
            stage.setScene(scene);
            stage.setTitle("GPA Calculator");
            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to load the Home screen.");
            e.printStackTrace();
        }
    }
}
