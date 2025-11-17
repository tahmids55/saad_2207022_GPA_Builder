package com.gpa.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// This is the main entry point for the JavaFX application.
public class GpaCalculatorApp extends Application {

    // The start method is the main entry point for all JavaFX applications.
    @Override
    public void start(Stage primaryStage) {
        try {
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


    // The main method is needed for some IDEs to launch the JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}

