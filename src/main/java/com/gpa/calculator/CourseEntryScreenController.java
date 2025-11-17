package com.gpa.calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

// This is the controller for the main course entry screen.
// It handles user input, validation, and adding courses to the list.
public class CourseEntryScreenController implements Initializable {

    // --- FXML UI Components ---
    @FXML private TableView<Course> courseTableView;
    @FXML private TableColumn<Course, String> courseNameCol;
    @FXML private TableColumn<Course, String> courseCodeCol;
    @FXML private TableColumn<Course, Integer> courseCreditCol;
    @FXML private TableColumn<Course, String> gradeCol;
    @FXML private TableColumn<Course, String> teacher1Col;
    @FXML private TableColumn<Course, String> teacher2Col;

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private ComboBox<Integer> courseCreditComboBox;
    @FXML private TextField teacher1NameField;
    @FXML private TextField teacher2NameField;
    @FXML private ComboBox<String> gradeComboBox;

    @FXML private Button calculateGpaButton;
    @FXML private Button backButton;
    @FXML private Button resetButton;
    @FXML private Label totalCreditsLabel;
    @FXML private Label currentGpaLabel;
    @FXML private Label errorLabel;
    @FXML private Label creditTargetLabel;
    @FXML private TextField creditTargetField;
    @FXML private Button setCreditTargetButton;
    @FXML private TableColumn<Course, Void> deleteCol;

    // --- Class Fields ---
    public static final ObservableList<Course> courseList = FXCollections.observableArrayList();
    private int requiredCredits = 15; // The total credits needed to enable GPA calculation (now modifiable).
    private int currentCredits = 0;

    // This method is automatically called after the FXML file has been loaded.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the table columns to display data from the Course objects.
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseCreditCol.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        teacher1Col.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Col.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));

        // Set up the delete column with delete buttons
        deleteCol.setCellFactory(param -> new TableCell<Course, Void>() {
            private final Button deleteButton = new Button("Delete");
            
            {
                deleteButton.setOnAction(event -> {
                    Course course = getTableView().getItems().get(getIndex());
                    deleteCourse(course);
                });
                deleteButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-background-radius: 4;");
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Populate the ComboBoxes with predefined values.
        gradeComboBox.setItems(FXCollections.observableArrayList("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F"));
        courseCreditComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        // Bind the table view to the observable list of courses.
        courseTableView.setItems(courseList);
        
        // Initialize the credit target field and label
        creditTargetLabel.setText("Target Credits: " + requiredCredits);
        creditTargetField.setText(String.valueOf(requiredCredits));

        // Initialize the view with any pre-loaded data
        updateViewWithExistingData();
    }

    // Handles the "Add Course" button click.
    @FXML
    private void handleAddCourseButtonAction() {
        // Validate input before proceeding.
        if (!validateInput()) {
            return;
        }

        // Create a new Course object from the form data.
        Course newCourse = new Course(
                courseNameField.getText(),
                courseCodeField.getText(),
                courseCreditComboBox.getValue(),
                teacher1NameField.getText(),
                teacher2NameField.getText(),
                gradeComboBox.getValue()
        );

        // Add the new course to the list and update the UI.
        courseList.add(newCourse);
        updateCreditsAndGpa();
        clearForm();
        errorLabel.setText("Course added successfully!");
    }

    // Validates that all required fields are filled.
    private boolean validateInput() {
        if (courseNameField.getText().isEmpty() ||
            courseCodeField.getText().isEmpty() ||
            teacher1NameField.getText().isEmpty() ||
            courseCreditComboBox.getValue() == null ||
            gradeComboBox.getValue() == null) {
            
            errorLabel.setText("Error: All fields except Teacher 2 must be filled.");
            return false;
        }
        // If Teacher 2 is empty, set it to "N/A"
        if (teacher2NameField.getText().isEmpty()) {
            teacher2NameField.setText("N/A");
        }
        errorLabel.setText(""); // Clear error message
        return true;
    }

    // Updates the credit total and GPA display.
    private void updateCreditsAndGpa() {
        currentCredits = courseList.stream().mapToInt(Course::getCourseCredit).sum();
        totalCreditsLabel.setText("Current Credits: " + currentCredits);

        double gpa = calculateGpa();
        currentGpaLabel.setText(String.format("Current GPA: %.2f", gpa));

        // Enable the "Calculate GPA" button if the credit requirement is met.
        if (currentCredits >= requiredCredits) {
            calculateGpaButton.setDisable(false);
        } else {
            calculateGpaButton.setDisable(true);
        }
    }
    
    // Populates the view with existing data from the static list.
    private void updateViewWithExistingData() {
        if (!courseList.isEmpty()) {
            updateCreditsAndGpa();
        }
    }


    // Clears all input fields after a course is added.
    private void clearForm() {
        courseNameField.clear();
        courseCodeField.clear();
        teacher1NameField.clear();
        teacher2NameField.clear();
        courseCreditComboBox.getSelectionModel().clearSelection();
        gradeComboBox.getSelectionModel().clearSelection();
    }

    // Handles the "Set Credit Target" button click.
    @FXML
    private void handleSetCreditTargetButtonAction() {
        try {
            int newTarget = Integer.parseInt(creditTargetField.getText());
            if (newTarget > 0) {
                requiredCredits = newTarget;
                creditTargetLabel.setText("Target Credits: " + requiredCredits);
                updateCreditsAndGpa();
                errorLabel.setText("Credit target updated successfully!");
            } else {
                errorLabel.setText("Error: Credit target must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Error: Please enter a valid number.");
        }
    }

    // Handles the "Back" button click to return to home screen.
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
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

    // Handles the "Reset" button click to clear all courses.
    @FXML
    private void handleResetCoursesButtonAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Reset");
        alert.setHeaderText("Clear All Courses");
        alert.setContentText("Are you sure you want to delete all courses? This action cannot be undone.");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            courseList.clear();
            updateCreditsAndGpa();
            errorLabel.setText("All courses have been reset!");
        }
    }

    // Handles individual course deletion.
    public void deleteCourse(Course course) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Course");
        alert.setContentText("Are you sure you want to delete " + course.getCourseName() + "?");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            courseList.remove(course);
            updateCreditsAndGpa();
            errorLabel.setText("Course deleted successfully!");
        }
    }

    // Handles the "Calculate GPA" button click.
    @FXML
    private void handleCalculateGpaButtonAction(ActionEvent event) {
        try {
            // Load the result screen FXML.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultScreen.fxml"));
            Parent resultRoot = loader.load();

            // Get the controller for the result screen.
            ResultScreenController resultController = loader.getController();
            
            // Pass the list of courses and the calculated GPA to the result screen.
            resultController.setResults(courseList, calculateGpa());

            // Switch the scene to the result screen.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(resultRoot);
            stage.setScene(scene);
            stage.setTitle("GPA Result");
            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to load the Result screen.");
            e.printStackTrace();
        }
    }

    // Calculates the final GPA based on the list of courses.
    private double calculateGpa() {
        double totalPoints = 0;
        int totalCredits = 0;

        for (Course course : courseList) {
            totalPoints += getGradePoints(course.getGrade()) * course.getCourseCredit();
            totalCredits += course.getCourseCredit();
        }

        return totalCredits == 0 ? 0 : totalPoints / totalCredits;
    }

    // Converts a letter grade (e.g., "A+", "B-") to grade points (e.g., 4.0, 2.7).
    private double getGradePoints(String grade) {
        switch (grade) {
            case "A+": return 4.0;
            case "A":  return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B":  return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C":  return 2.0;
            case "D":  return 1.0;
            case "F":  return 0.0;
            default:   return 0.0;
        }
    }
}

