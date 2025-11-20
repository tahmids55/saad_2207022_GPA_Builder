# GPA Calculator

This is a JavaFX application for calculating GPA based on course credits and grades, as specified in the project requirements. The application now includes SQLite database management for persistent student records and course data.

## Project Structure

The project follows a standard Maven and FXML structure:

- `pom.xml`: Defines project dependencies (JavaFX, SQLite JDBC) and build configurations.
- `src/main/java/com/gpa/calculator/`: Contains the Java source code.
  - `GpaCalculatorApp.java`: The main application class that launches the app.
  - `Course.java`: The model class representing a single course.
  - `Student.java`: The model class representing a student record.
  - `DatabaseManager.java`: Handles all SQLite database operations.
  - `CurrentStudent.java`: Helper class to maintain the current logged-in student.
  - `*Controller.java`: Controller classes that handle the logic for each FXML screen.
- `src/main/resources/com/gpa/calculator/`: Contains the FXML view files and CSS stylesheets.
  - `*.fxml`: The UI layout files for each screen.
  - `styles.css`: The stylesheet for the application.

## Database Features

### Overview
The application now uses SQLite to persistently store student information and course data. Each student is uniquely identified by their roll number and can store multiple course entries along with the calculated GPA.

### Database Schema

#### Students Table
- `id` (INTEGER, PRIMARY KEY): Auto-incremented student ID
- `roll_number` (TEXT, UNIQUE): Student's unique roll number (main identifier)
- `name` (TEXT): Student's full name
- `gpa` (REAL): The calculated GPA (defaults to 0.0)
- `created_at` (TIMESTAMP): Record creation timestamp

#### Courses Table
- `id` (INTEGER, PRIMARY KEY): Auto-incremented course ID
- `student_id` (INTEGER, FOREIGN KEY): Reference to the student
- `course_name` (TEXT): Name of the course
- `course_code` (TEXT): Course code/number
- `credit` (REAL): Credit hours for the course
- `teacher1_name` (TEXT): Primary instructor name
- `teacher2_name` (TEXT): Secondary instructor name (optional)
- `grade` (TEXT): Letter grade received
- `created_at` (TIMESTAMP): Record creation timestamp

### Key Features

1. **Student Login/Registration**
   - Enter roll number and name on the login screen
   - New students are automatically registered in the database
   - Existing students have their records loaded from the database

2. **Course Management**
   - Add, view, and delete courses for a student
   - All course data is persisted to the database
   - Each course is linked to a specific student via their roll number

3. **GPA Calculation & Storage**
   - Calculate GPA based on courses and grades
   - Final GPA is automatically saved to the database
   - Students can log in later and view their courses and GPA

4. **Data Persistence**
   - SQLite database file (`gpa_calculator.db`) is created automatically in the project directory
   - All data survives application restarts
   - Students can retrieve their previous entries by logging in with the same roll number

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

## Application Workflow

1. **Home Screen**: Welcome screen with "Start GPA Calculator" button
2. **Login Screen**: Enter your roll number and name
   - New students are automatically created in the database
   - Existing students have their data loaded
3. **Course Entry Screen**: 
   - Add courses with details (name, code, credits, grades, instructors)
   - View previously entered courses
   - Delete individual courses or all courses
   - Set credit target for GPA calculation
4. **Result Screen**:
   - View calculated GPA
   - View course summary
   - Student information and GPA are saved to the database
   - Start over to log in as a different student

## Technologies Used

- **JavaFX 17.0.2**: UI framework
- **SQLite 3.45.0.0**: Lightweight embedded database
- **Maven**: Build and dependency management
- **Java 11+**: Programming language


