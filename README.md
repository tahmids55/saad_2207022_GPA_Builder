# GPA Calculator

This is a JavaFX application for calculating GPA based on course credits and grades, as specified in the project requirements.

## Project Structure

The project follows a standard Maven and FXML structure:

- `pom.xml`: Defines project dependencies (JavaFX) and build configurations.
- `src/main/java/com/gpa/calculator/`: Contains the Java source code.
  - `GpaCalculatorApp.java`: The main application class that launches the app.
  - `Course.java`: The model class representing a single course.
  - `*Controller.java`: Controller classes that handle the logic for each FXML screen.
- `src/main/resources/com/gpa/calculator/`: Contains the FXML view files and CSS stylesheets.
  - `*.fxml`: The UI layout files for each screen.
  - `styles.css`: The stylesheet for the application.

## How to Run the Application

1.  **Prerequisites:**
    - Java JDK 11 or later must be installed.
    - Apache Maven must be installed.

2.  **Navigate to the project directory:**
    ```bash
    cd GPACalculator
    ```

3.  **Compile and Run:**
    Use the JavaFX Maven plugin to run the application:
    ```bash
    mvn clean javafx:run
    ```

