import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * The StudentMenuPane class represents a graphical user interface component
 * that displays
 * various options and information for a student.
 * It extends the VBox class and provides functionalities for registering
 * courses,
 * viewing past and current courses, proceeding to the next trimester, and
 * logging out.
 */
public class StudentMenuPane extends VBox {
    private Stage primaryStage;
    private Scene studentMenuScene;
    private Set<Course> courses;
    private String studentID;
    private int currentTrimester;
    private int trimesterProceedCount = 0;

     /**
     * Constructs a new StudentMenuPane with the specified primary stage and student ID.
     *
     * @param primaryStage the primary stage of the application
     * @param userID the ID of the student
     */
    public StudentMenuPane(Stage primaryStage, String userID) {

        this.primaryStage = primaryStage;
        this.courses = CourseUtil.readCourseFromCourseFile();
        this.studentID = userID;

        currentTrimester = getTri(studentID);
        Label informationLabel = new Label("Your are currently in Trimester " + currentTrimester);
        Button registerButton = new Button("Register Course");
        registerButton.setOnAction(e -> registerCourses());

        Button viewPastButton = new Button("View Past Registered Course");
        viewPastButton.setOnAction(e -> viewPassCourses());

        Button viewCurrentButton = new Button("View Current Course");
        viewCurrentButton.setOnAction(e -> viewCurrentCourses());

        Button viewFutureButton = new Button("View Future Course");
        viewFutureButton.setOnAction(e -> viewFutureCourses());

        Button proceedNextTriButton = new Button("Proceed to Next Trimester");
        proceedNextTriButton.setOnAction(e -> proceedNextTri(informationLabel, currentTrimester));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        setAlignment(Pos.CENTER);
        getChildren().addAll(informationLabel, registerButton, viewPastButton, viewCurrentButton, viewFutureButton,
                proceedNextTriButton, logoutButton);

    }

    /**
     * Sets the scene of the StudentMenuPane.
     *
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        this.studentMenuScene = scene;
    }

     /**
     * Retrieves the current scene associated with the StudentMenuPane.
     *
     * @return the current scene
     */
    public Scene getCurrentScene() {
        // studentMenuScene = new Scene(this, 600, 400);
        return studentMenuScene;
    }

     /**
     * Opens the registration window for the student to register for courses.
     * Invokes the StudentRegisterCourse class to handle the registration process.
     */
    private void registerCourses() {
        StudentRegisterCourse registerCourse = new StudentRegisterCourse(primaryStage, studentID);
        registerCourse.registerCourse(courses, studentID, trimesterProceedCount);
        primaryStage.setScene(new Scene(registerCourse, 600, 400));
    }

      /**
     * Displays the window to view past registered courses for the student.
     * Invokes the StudentViewCourse class to handle the viewing process.
     */
    private void viewPassCourses() {
        StudentViewCourse viewCourse = new StudentViewCourse(primaryStage, studentID);
        viewCourse.passRegistedCourse(studentID);
        primaryStage.setScene(new Scene(viewCourse, 600, 400));
    }

    /**
     * Displays the window to view current courses for the student.
     * Invokes the StudentViewCourse class to handle the viewing process.
     */
    private void viewCurrentCourses() {
        StudentViewCourse viewCourse = new StudentViewCourse(primaryStage, studentID);
        viewCourse.CurrentCourse(studentID);
        primaryStage.setScene(new Scene(viewCourse, 600, 400));
    }

    /**
     * Displays the window to view future courses for the student.
     * Invokes the StudentViewCourse class to handle the viewing process.
     */
    private void viewFutureCourses() {
        StudentViewCourse viewCourse = new StudentViewCourse(primaryStage, studentID);
        viewCourse.futureCourse(studentID);
        primaryStage.setScene(new Scene(viewCourse, 600, 400));
    }

    /**
     * Proceeds the student to the next trimester if the conditions are met,
     * updates the UI accordingly, and performs necessary data storage operations.
     *
     * @param informationLabel the label displaying information about the current trimester
     * @param currentTrimester the current trimester of the student
     */
    private void proceedNextTri(Label informationLabel, int currentTrimester) {
        boolean validCreditHours = true;
        if (currentTrimester <= 2) {
            for (Course course : courses) {
                validCreditHours = checkCreditHoursForNextTri(course.courseCode, studentID);

                if (!validCreditHours) {
                    showAlert("Not enough credit", "Please ensure you have taken 3-12 credit hours in a trimester.");
                    return;
                }
            }

            if (currentTrimester < 3) {
                currentTrimester = currentTrimester + 1;
            } else {
                return;
            }
            informationLabel.setText("Your are currently in Trimester " + currentTrimester);

            if (!isStudentExist(studentID)) {
                storePassRegisteredCourse(studentID, currentTrimester);
                clearCurrentCourse(studentID);
            } else {
                updateCourse(studentID, currentTrimester);
                clearCurrentCourse(studentID);
            }
            showAlert("Success", "Proceeded to next trimester successfully.");
        } else {
            Alert finishAlert = new Alert(AlertType.ERROR);
            finishAlert.setTitle("Error");
            finishAlert.setContentText("You have already finished your 3 trimesters.");
            finishAlert.showAndWait();
            return;
        }
    }

    /**
     * Displays an alert dialog with the specified title and content.
     * After displaying the alert, resets the scene to the student menu scene.
     *
     * @param title   the title of the alert dialog
     * @param content the content text of the alert dialog
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        if (studentMenuScene != null) {
            primaryStage.setScene(studentMenuScene);
        }
    }

     /**
     * Retrieves the current trimester of the student from the passRegister.csv file.
     *
     * @param studentID the ID of the student
     * @return the current trimester of the student
     */
    public static int getTri(String studentID) {
        // Initialize with a default value
        int defaultTri = 1;
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("passRegister.csv"));
            for (String studentLine : studentLines) {
                String[] studentItems = studentLine.split(",");
                if (studentItems.length >= 2 && studentItems[1].equals(studentID)) {
                    int trimester = Integer.parseInt(studentItems[0]);
                    return trimester;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        // Return the default value if no match found
        return defaultTri;
    }

    /**
     * Checks if the student has taken the required credit hours for the next trimester.
     * Reads the student's credit hours from the student.csv file and verifies if they meet the criteria.
     *
     * @param selectedCourse the course code of the selected course
     * @param studentId      the ID of the student
     * @return true if the student has taken the required credit hours for the next trimester, otherwise false
     */
    private boolean checkCreditHoursForNextTri(String selectedCourse, String studentId) {
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            for (String studentLine : studentLines) {
                String[] studentItems = studentLine.split(",");
                if (studentItems[0].equals(studentId)) {
                    int currentCreditHours = Integer.parseInt(studentItems[2]);
                    if (currentCreditHours > 12) {
                        System.out.println("\nCredit hours exceeded the limit of 12.\n");
                        return false;
                    } else if (currentCreditHours < 3) {
                        System.out.println("\nCredit hours should be at least 3.\n");
                        return false;
                    } else if (currentCreditHours == 12) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

     /**
     * Checks if a student with the specified ID exists in the passRegister.csv file.
     *
     * @param studentID the ID of the student
     * @return true if the student exists in the file, otherwise false
     */
    private boolean isStudentExist(String studentID) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("passRegister.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items.length > 1 && items[1].equals(studentID)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

      /**
     * Stores the pass registered course information for the student in the passRegister.csv file.
     * Appends the new registration to the existing file.
     *
     * @param studentId          the ID of the student
     * @param currentTrimester   the current trimester of the student
     */
    private void storePassRegisteredCourse(String studentId, int currentTrimester) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(studentId)) {
                    StringBuilder updatedLine = new StringBuilder();
                    updatedLine.append(currentTrimester).append(",");
                    for (int i = 0; i < parts.length; i++) {
                        updatedLine.append(parts[i]);
                        if (i != parts.length - 1) {
                            updatedLine.append(",");
                        }
                    }
                    updatedLines.add(updatedLine.toString());
                }
            }
            Files.write(Paths.get("passRegister.csv"), updatedLines, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

      /**
     * Clears the current courses of the student in the student.csv file.
     * Sets the credit hours to 0 for the specified student.
     *
     * @param studentId the ID of the student
     */
    private void clearCurrentCourse(String studentId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(studentId)) {
                    StringBuilder updatedLine = new StringBuilder(parts[0] + "," + parts[1] + ",0,");
                    updatedLines.add(updatedLine.toString());
                } else {
                    updatedLines.add(line);
                }
            }
            Files.write(Paths.get("student.csv"), updatedLines);
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

     /**
     * Updates the course information for the student in the passRegister.csv file.
     * Increments the credit hours and appends the new course registration if necessary.
     *
     * @param studentId          the ID of the student
     * @param currentTrimester   the current trimester of the student
     */
    private void updateCourse(String studentId, int currentTrimester) {
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            List<String> passRegisterLines = Files.readAllLines(Paths.get("passRegister.csv"));

            for (String studentLine : studentLines) {
                String[] studentData = studentLine.split(",");
                if (studentData.length > 0 && studentData[0].equals(studentId)) {
                    int creditHour = Integer.parseInt(studentData[2]);
                    TreeSet<String> courseCodes = new TreeSet<>();
                    for (int i = 3; i < studentData.length; i++) {
                        courseCodes.add(studentData[i]);
                    }
                    for (int i = 0; i < passRegisterLines.size(); i++) {
                        String[] passRegisterData = passRegisterLines.get(i).split(",");
                        if (passRegisterData.length > 1 && passRegisterData[1].equals(studentId)) {
                            int existingNextTri = currentTrimester;
                            passRegisterData[0] = String.valueOf(existingNextTri);
                            int existingCreditHour = Integer.parseInt(passRegisterData[3]);
                            passRegisterData[3] = String.valueOf(existingCreditHour + creditHour);
                            passRegisterLines.set(i, String.join(",", passRegisterData));
                            for (String courseCode : courseCodes) {
                                if (!passRegisterLines.get(i).contains(courseCode)) {
                                    passRegisterLines.set(i, passRegisterLines.get(i) + "," + courseCode);
                                }
                            }
                            break;
                        }
                    }
                }
            }
            Files.write(Paths.get("passRegister.csv"), passRegisterLines, StandardOpenOption.WRITE);
        } catch (IOException ex) {
            System.out.println("An error occurred while updating the credit hours: " + ex.getMessage());
        }
    }

    /**
     * Handles the logout action of the student.
     * Invokes the Logout class to confirm the logout action.
     */
    private void handleLogout() {
        Logout logout = new Logout(primaryStage);
        logout.confirmLogout();
    }

}
