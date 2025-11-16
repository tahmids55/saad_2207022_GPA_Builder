package com.gpa.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

// Controller for the Home Screen (HomeScreen.fxml).
// Its primary responsibility is to handle the action of the "Start" button.
public class HomeScreenController {

    // This method is called when the "Start GPA Calculator" button is clicked.
    @FXML
    private void handleStartButtonAction(ActionEvent event) {
        try {
            // Load the FXML file for the course entry screen.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseEntryScreen.fxml"));
            Parent courseEntryRoot = loader.load();

            // Get the current stage (window) from the event source.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the course entry screen layout.
            Scene scene = new Scene(courseEntryRoot);
            
            // Set the new scene on the stage.
            stage.setScene(scene);
            stage.setTitle("Enter Course Details");
            stage.show();

        } catch (IOException e) {
            // Print an error message if the FXML file cannot be loaded.
            System.err.println("Failed to load the Course Entry screen.");
            e.printStackTrace();
        }
    }
}

