import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert.AlertType;

/**
 * The StudentRegisterCourse class represents a graphical user interface
 * component
 * for students to register for courses. It extends the VBox class and provides
 * functionalities to register for courses, check credit hours, prerequisites,
 * and manage the registration process.
 */
public class StudentRegisterCourse extends VBox {
    private String studentID;
    private Stage primaryStage;
    Set<String> registeredCourses = getRegisteredCourses();
    private Scene studentMenuScene;

    /**
     * Constructs a new StudentRegisterCourse instance with default values.
     */
    public StudentRegisterCourse() {
    }

    /**
     * Constructs a new StudentRegisterCourse instance with the specified primary
     * stage
     * and student ID.
     *
     * @param primaryStage the primary stage of the application
     * @param studentID    the ID of the student
     */
    public StudentRegisterCourse(Stage primaryStage, String studentID) {
        this.primaryStage = primaryStage;
        this.studentID = studentID;
        this.registeredCourses = getRegisteredCourses();
    }

    /**
     * Sets the scene for the student menu.
     *
     * @param studentMenuScene the scene for the student menu
     */
    public void setScene(Scene studentMenuScene) {
        this.studentMenuScene = studentMenuScene;
    }

    /**
     * Registers a course for the student.
     *
     * @param courses               the set of courses available for registration
     * @param userID                the ID of the student
     * @param trimesterProceedCount the number of trimesters the student has
     *                              proceeded
     */
    public void registerCourse(Set<Course> courses, String userID, int trimesterProceedCount) {
        this.studentID = userID;
        Button backtoMainButton = new Button("Back");
        if (trimesterProceedCount >= 3) {
            Alert finishAlert = new Alert(AlertType.ERROR);
            finishAlert.setTitle("Error");
            finishAlert.setContentText("You have already finished your 3 trimesters.");
            finishAlert.showAndWait();
            return;
        }

        ChoiceDialog<Course> courseDialog = new ChoiceDialog<>(null, courses);
        courseDialog.setTitle("Select Course to Register");
        courseDialog.setHeaderText("List of Courses");
        courseDialog.setContentText("Select a Course:");
        ButtonType viewButton = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType backButton = new ButtonType("Back", ButtonData.CANCEL_CLOSE);
        courseDialog.getDialogPane().getButtonTypes().clear();
        courseDialog.getDialogPane().getButtonTypes().setAll(viewButton, backButton);
        List<Course> CourseList = courses.stream()
                .map(course -> new Course(course.getCourseCode())) // Assuming Course has a constructor that takes a
                                                                   // course code
                .collect(Collectors.toList());

        courseDialog.getItems().clear(); // Clear existing items
        courseDialog.getItems().addAll(CourseList); // Add filtered courses
        courseDialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {

            return;
        });
        Optional<Course> courseResult = courseDialog.showAndWait();
        if (courseResult.isEmpty()) {
            primaryStage.close();
            StudentDashboardFx studentDashboard = new StudentDashboardFx(userID);
            studentDashboard.start(new Stage());

        }
        courseResult.ifPresent(selectedCourse -> {
            String selectedCourseCode = selectedCourse.getCourseCode();
            Course selectedCourses = findCourse(courses, selectedCourseCode);

            if (selectedCourse != null) {

                boolean studentAlreadyRegistered = checkCourseCode(studentID, selectedCourseCode);

                // check credit hours
                if (!checkCreditHours(selectedCourseCode, studentID)) {
                    primaryStage.close();
                    StudentDashboardFx studentDashboard = new StudentDashboardFx(userID);
                    studentDashboard.start(new Stage());
                    return;
                }
                // check prerequisites
                if (!checkPreRequisites(selectedCourseCode, this.studentID)) {

                    registerCourse(courses, studentID, trimesterProceedCount);
                    return;
                }

                if (studentAlreadyRegistered == false) {
                    registeredCourses.add(selectedCourseCode);
                    studentAlreadyRegistered = selectedCourse.studentsEnrolled.contains(this);

                    selectedCourse.studentsEnrolled.add(new Student(this.studentID, null));
                    addCourseToStudentFile(studentID, selectedCourse);
                    addCreditHours(selectedCourseCode);
                    removeSame(this.studentID);
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Course registered successfully.");
                    ButtonType mainMenuButton = new ButtonType("Main Menu");
                    ButtonType registerAnotherButton = new ButtonType("Register Another");
                    alert.getButtonTypes().setAll(registerAnotherButton, mainMenuButton);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == registerAnotherButton) {
                            registerCourse(courses, studentID, trimesterProceedCount);

                        } else if (response == mainMenuButton) {
                            primaryStage.close();
                            StudentDashboardFx studentDashboard = new StudentDashboardFx(studentID);
                            studentDashboard.start(new Stage());
                        }
                    });

                } else {
                    unsuccessfulRegisterAlert(selectedCourseCode);
                    registerCourse(courses, studentID, trimesterProceedCount);
                    // primaryStage.setScene(new Scene(registerCourse, 600, 400));
                }
            }

            getChildren().clear();

            backtoMainButton.setOnAction(e -> {
                primaryStage.close();
                StudentDashboardFx studentDashboard = new StudentDashboardFx(studentID);
                studentDashboard.start(primaryStage);
                return;
            });
            setAlignment(Pos.CENTER);
        });
    }

    /**
     * Displays an alert for unsuccessful course registration.
     *
     * @param selectedCourseCode the code of the selected course
     */
    public void unsuccessfulRegisterAlert(String selectedCourseCode) {
        // Scene adminMenuScene = primaryStage.getScene();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Duplicate registration");
        alert.setContentText("Student already registered for the course " + selectedCourseCode + ".\n"
                + "Please select another course for registration.");
        alert.showAndWait();
        return;
    }

    /**
     * Retrieves the set of registered courses for the student.
     *
     * @return the set of registered courses
     */
    public Set<String> getRegisteredCourses() {
        registeredCourses = new LinkedHashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[0].equals(studentID)) {
                    for (int i = 3; i < items.length; i++) {
                        registeredCourses.add(items[i]);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return registeredCourses;
    }

    /**
     * Finds a course with the specified course code from the set of courses.
     *
     * @param courses    the set of courses to search
     * @param courseCode the code of the course to find
     * @return the course object if found, otherwise null
     */
    public Course findCourse(Set<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Checks if a course with the specified course code is already registered by
     * the student.
     *
     * @param studentID      the ID of the student
     * @param selectedCourse the code of the selected course
     * @return true if the course is already registered, otherwise false
     */
    public static boolean checkCourseCode(String studentID, String selectedCourse) {
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            List<String> passRegisterLines = Files.readAllLines(Paths.get("passRegister.csv"));

            // Check if the course is not in passRegister.csv
            for (String passLine : passRegisterLines) {
                String[] passParts = passLine.split(",");
                if (passParts.length > 1 && passParts[1].equals(studentID)) {
                    for (int i = 2; i < passParts.length; i++) {
                        if (passParts[i].equals(selectedCourse)) {
                            // System.out.println("The course " + selectedCourse + " is already in
                            // passRegister.csv.");
                            return true;
                        }
                    }
                }
            }

            // Check if the course is not in student.csv
            for (String studentLine : studentLines) {
                String[] studentParts = studentLine.split(",");
                if (studentParts[0].equals(studentID)) {
                    for (int i = 3; i < studentParts.length; i++) {
                        if (studentParts[i].equals(selectedCourse)) {
                            // System.out.println("The course " + selectedCourse + " is already in
                            // student.csv.");
                            return true;
                        }
                    }
                }
            }

            // If the course is not found in passRegister.csv or student.csv, return true
            return false;
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
            return true;
        }
    }

    /**
     * Removes duplicate registrations for the same course from the student's
     * record.
     *
     * @param studentID the ID of the student
     */
    public static void removeSame(String studentID) {
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            List<String> passRegisterLines = Files.readAllLines(Paths.get("passRegister.csv"));

            List<String> updatedStudentLines = new ArrayList<>();

            for (String studentLine : studentLines) {
                String[] studentParts = studentLine.split(",");
                if (studentParts.length > 0 && studentParts[0].equals(studentID)) {
                    List<String> updatedCourses = new ArrayList<>();

                    for (int i = 2; i < studentParts.length; i++) {
                        boolean found = false;

                        for (String passLine : passRegisterLines) {
                            String[] passParts = passLine.split(",");
                            if (passParts.length > 0 && passParts[0].equals(studentID)) {
                                for (int j = 2; j < passParts.length; j++) {
                                    if (studentParts[i].equals(passParts[j])) {
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (found) {
                                break;
                            }
                        }

                        if (!found) {
                            updatedCourses.add(studentParts[i]);
                        }
                    }

                    String updatedLine = String.join(",", studentParts[0], studentParts[1]);
                    if (!updatedCourses.isEmpty()) {
                        updatedLine += "," + String.join(",", updatedCourses);
                    }
                    updatedStudentLines.add(updatedLine);
                } else {
                    updatedStudentLines.add(studentLine);
                }
            }

            Files.write(Paths.get("student.csv"), updatedStudentLines);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Reads the student file to retrieve student data.
     */
    public static void readStudentFile() {
        try {

            List<String> lines = Files.readAllLines(Paths.get("user.csv"));
            List<String> firstColumn = new ArrayList<>();
            for (String line : lines) {
                String[] items = line.split(",");
                firstColumn.add(items[0]);
            }
            Files.write(Paths.get("student.csv"), firstColumn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Checks if adding the selected course exceeds the credit hour limit for the
     * student.
     *
     * @param selectedCourse the code of the selected course
     * @param studentId      the ID of the student
     * @return true if the credit hours are within limits, otherwise false
     */
    public static boolean checkCreditHours(String selectedCourse, String studentId) {
        int creditHours = 0;
        // get credit hours from course.csv
        try {
            List<String> lines = Files.readAllLines(Paths.get("course.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[1].equals(selectedCourse)) {
                    creditHours = Integer.parseInt(items[0]);
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        // get credit hour column from student.csv
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            for (String studentLine : studentLines) {
                String[] studentItems = studentLine.split(",");
                if (studentItems[0].equals(studentId)) {
                    int currentCreditHours = Integer.parseInt(studentItems[2]);
                    if (currentCreditHours + creditHours > 12) {
                        // System.out.println("\nCredit hours exceeded the limit of 12.\n");
                        exceedAlert();
                        return false;
                    } else if (currentCreditHours + creditHours < 3) {
                        // System.out.println("\nCredit hours should be at least 3.\n");
                        belowAlert();
                        return false;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    /**
     * Displays an alert indicating that the credit hours limit has been exceeded.
     */
    public static void exceedAlert() {
        Alert exceedAlert = new Alert(AlertType.ERROR);
        exceedAlert.setTitle("Error");
        exceedAlert.setContentText("Credit hours exceeded the limit of 12.\n Returning to main menu.");
        exceedAlert.showAndWait();
        return;
    }

    /**
     * Displays an alert indicating that the credit hours should be at least 3.
     */
    public static void belowAlert() {
        Alert belowAlert = new Alert(AlertType.ERROR);
        belowAlert.setTitle("Error");
        belowAlert.setContentText("Credit hours should be at least 3");
        belowAlert.showAndWait();
        return;
    }

    /**
     * Checks if the student has enough credit hours to proceed to the next
     * trimester.
     *
     * @param selectedCourse the code of the selected course
     * @param studentId      the ID of the student
     * @return true if the student has enough credit hours, otherwise false
     */
    public static boolean checkCreditHoursForNextTri(String selectedCourse, String studentId) {
        // get credit hour column from student.csv
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            for (String studentLine : studentLines) {
                String[] studentItems = studentLine.split(",");
                if (studentItems[0].equals(studentId)) {
                    int currentCreditHours = Integer.parseInt(studentItems[2]);
                    if (currentCreditHours > 12) {
                        // System.out.println("\nCredit hours exceeded the limit of 12.\n");
                        exceedAlert();
                        return false;
                    } else if (currentCreditHours < 3) {
                        // System.out.println("\nCredit hours should be at least 3.\n");
                        belowAlert();
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
     * Adds a course to the student file.
     *
     * @param studentId the ID of the student
     * @param course    the course to add
     */
    private static void addCourseToStudentFile(String studentId, Course course) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            List<String> updatedLines = new ArrayList<>();

            // Iterate through each line in the file
            for (String line : lines) {
                String[] parts = line.split(","); // Split the line by comma

                // Check if the first part (student ID) matches the given studentId
                if (parts.length > 0 && parts[0].equals(studentId)) {
                    // Append the course code to the current line
                    StringBuilder updatedLine = new StringBuilder(line);
                    updatedLine.append(course.courseCode).append(",");
                    updatedLines.add(updatedLine.toString());

                } else {
                    updatedLines.add(line); // Keep the line unchanged
                }
            }

            // Write the updated content back to the file
            Files.write(Paths.get("student.csv"), updatedLines);
            // System.out.println("Course added successfully for student: " + studentId);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Adds credit hours for the selected course to the student's record.
     *
     * @param selectedCourse the code of the selected course
     */
    public void addCreditHours(String selectedCourse) {
        int creditHours = 0;
        // get credit hours from course.csv
        try {
            List<String> lines = Files.readAllLines(Paths.get("course.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[1].equals(selectedCourse)) {
                    creditHours = Integer.parseInt(items[0]);
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        // get credit hour column from student.csv
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            List<String> updatedStudentLines = new ArrayList<>();
            for (String studentLine : studentLines) {
                String[] studentItems = studentLine.split(",");
                if (studentItems[0].equals(studentID)) {
                    int currentCreditHours = Integer.parseInt(studentItems[2]);
                    int newCreditHours = currentCreditHours + creditHours;
                    studentItems[2] = String.valueOf(newCreditHours); // Update credit hours
                    studentLine = studentItems[0] + "," + studentItems[1] + "," + studentItems[2] + ","
                            + String.join(",", registeredCourses);
                }
                updatedStudentLines.add(studentLine);
            }

            // Write the updated student data back to student.csv
            Files.write(Paths.get("student.csv"), updatedStudentLines);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Checks if the prerequisites for the selected course are met.
     *
     * @param courseCode the code of the selected course
     * @param studentId  the ID of the student
     * @return true if prerequisites are met, otherwise false
     */
    public static boolean checkPreRequisites(String courseCode, String studentId) {
        if (courseCode.equals("CS214"))
            return cs214Checker(studentId);
        else if (courseCode.equals("CS224"))
            return cs224Checker(studentId);
        else if (courseCode.equals("CS316"))
            return cs316Checker(studentId);
        else
            return true;
    }

    /**
     * Checks if the prerequisites for CS214 are met.
     *
     * @param studentId the ID of the student
     * @return true if CS113 is registered, otherwise false
     */
    public static boolean cs214Checker(String studentId) {
        try {
            // List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            List<String> passLines = Files.readAllLines(Paths.get("passRegister.csv"));

            for (String pass : passLines) {
                String[] parts = pass.split(",");
                if (parts.length > 1 && parts[1].equals(studentId)) {
                    for (int i = 4; i < parts.length; i++) {
                        if (parts[i].equals("CS113"))
                            return true;
                    }
                }
            }
            // System.out.println("\nYou need to register CS113 before taking CS214.\n");
            Alert requireAlert = new Alert(AlertType.ERROR);
            requireAlert.setTitle("Error");
            requireAlert.setContentText("You need to register CS113 before taking CS214.");
            requireAlert.showAndWait();
            return false;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    /**
     * Checks if the prerequisites of CS224 ae met.
     *
     * @param studentId the ID of the student
     * @return true if CS123 is registered, otherwise false
     */
    public static boolean cs224Checker(String studentId) {
        try {
            // List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            List<String> passLines = Files.readAllLines(Paths.get("passRegister.csv"));

            for (String pass : passLines) {
                String[] parts = pass.split(",");
                if (parts.length > 1 && parts[1].equals(studentId)) {
                    for (int i = 4; i < parts.length; i++) {
                        if (parts[i].equals("CS123"))
                            return true;
                    }
                }
            }

            // System.out.println("\nYou need to register CS123 before taking CS224.\n");
            Alert requireAlert = new Alert(AlertType.ERROR);
            requireAlert.setTitle("Error");
            requireAlert.setContentText("You need to register CS123 before taking CS224.");
            requireAlert.showAndWait();
            return false;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    /**
     * Checks if the prerequisites for CS316 are met.
     *
     * @param studentId the ID of the student
     * @return true if prerequisites are met, otherwise false
     */
    public static boolean cs316Checker(String studentId) {
        boolean cs133Registered = false;
        boolean cs214Registered = false;

        try {
            List<String> lines = Files.readAllLines(Paths.get("passRegister.csv"));

            int temp = 0;
            for (String line : lines) {
                String[] items = line.split(",");
                if (items.length > 1 && items[1].equals(studentId)) {
                    temp = Integer.parseInt(items[3]);
                    break;
                }
            }

            if (temp >= 15) {
                for (String line : lines) {
                    String[] items = line.split(",");
                    if (items.length > 1 && items[1].equals(studentId)) {
                        for (int i = 4; i < items.length; i++) {
                            if (items[i].equals("CS133")) {
                                cs133Registered = true;
                            } else if (items[i].equals("CS214")) {
                                cs214Registered = true;
                            }
                        }
                        break;
                    }
                }

                if (!cs133Registered) {
                    System.out.println("\nYou need to register CS133 before taking CS316.\n");
                    Alert requireAlert = new Alert(AlertType.ERROR);
                    requireAlert.setTitle("Error");
                    requireAlert.setContentText("You need to register CS133 before taking CS316.");
                    requireAlert.showAndWait();
                    return false;
                }

                if (!cs214Registered) {
                    System.out.println("\nYou need to register CS214 before taking CS316.\n");
                    Alert requireAlert = new Alert(AlertType.ERROR);
                    requireAlert.setTitle("Error");
                    requireAlert.setContentText("You need to register CS214 before taking CS316.");
                    requireAlert.showAndWait();
                    return false;
                }

            } else {
                System.out.println("\nYou need at least 15 credit hours before taking CS316.\n");
                Alert requireAlert = new Alert(AlertType.ERROR);
                requireAlert.setTitle("Error");
                requireAlert.setContentText("You need at least 15 credit hours before taking CS316.");
                requireAlert.showAndWait();
                return false;
            }

            return true;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

}
