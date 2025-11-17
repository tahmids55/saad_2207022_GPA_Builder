package com.gpa.calculator;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// This controller manages the result screen (ResultScreen.fxml).
// It displays the final GPA and the summary of entered courses.
public class ResultScreenController implements Initializable {

    // --- FXML UI Components ---
    @FXML private Label gpaLabel;
    @FXML private TableView<Course> resultTableView;
    @FXML private TableColumn<Course, String> courseNameCol;
    @FXML private TableColumn<Course, String> courseCodeCol;
    @FXML private TableColumn<Course, Integer> courseCreditCol;
    @FXML private TableColumn<Course, String> gradeCol;
    @FXML private TableColumn<Course, String> teacher1Col;
    @FXML private TableColumn<Course, String> teacher2Col;

    // This method is called automatically after the FXML is loaded.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the table columns to bind to the Course objects properties.
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseCreditCol.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        teacher1Col.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Col.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
    }

    // This public method is called by the CourseEntryScreenController to pass data.
    public void setResults(ObservableList<Course> courses, double gpa) {
        // Display the final GPA, formatted to two decimal places.
        gpaLabel.setText(String.format("Your Final GPA is: %.2f", gpa));
        // Populate the table with the list of courses.
        resultTableView.setItems(courses);
    }

    // Handles the "Start Over" button click.
    @FXML
    private void handleStartOverButtonAction(ActionEvent event) {
        try {
            // Clear the course list for a fresh start
            CourseEntryScreenController.courseList.clear();
            
            // Load the home screen FXML.
            Parent homeScreenRoot = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
            
            // Get the current stage and set the scene back to the home screen.
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

