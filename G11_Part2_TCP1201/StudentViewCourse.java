import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Represents a view for displaying various types of courses for a student.
 * The displayed courses include passed, current, and future courses.
 */
public class StudentViewCourse extends VBox {
    private Stage primaryStage;
    private String studentID;

    /**
     * Constructs a StudentViewCourse object with the specified primaryStage and
     * studentID.
     * 
     * @param primaryStage The primary stage of the application.
     * @param studentID    The ID of the student for whom the courses are displayed.
     */
    public StudentViewCourse(Stage primaryStage, String studentID) {
        this.primaryStage = primaryStage;
        this.studentID = studentID;

    }

    /**
     * Displays the courses that the student has passed.
     * 
     * @param studentID The ID of the student.
     */
    public void passRegistedCourse(String studentID) {
        this.studentID = studentID;

        try {
            Label title = new Label("Pass Enrollment Course for student " + studentID + ":");
            List<String> lines = Files.readAllLines(Paths.get("passRegister.csv"));
            TableView<Course> passSubject = new TableView<>();
            ObservableList<Course> passList = FXCollections.observableArrayList();
            for (String line : lines) {
                String[] items = line.split(",");
                for (int i = 4; i < items.length; i++) {

                    if (items[1].equals(studentID))
                        // System.out.println(items[i]);
                        passList.add(new Course(items[i]));
                }

            }
            Button backButton = new Button("Back");
            TableColumn<Course, String> column1 = new TableColumn<>("CourseCode");
            column1.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            passSubject.getColumns().add(column1);
            passSubject.setItems(passList);
            passSubject.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
            getChildren().clear();

            getChildren().addAll(title, passSubject, backButton);
            backButton.setOnAction(e -> {
                StudentDashboardFx studentDashboard = new StudentDashboardFx(studentID);
                studentDashboard.start(primaryStage);
                return;

            });
            setAlignment(Pos.CENTER);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Displays the courses that the student is currently enrolled in.
     * 
     * @param studentID The ID of the student.
     */
    public void CurrentCourse(String studentID) {

        this.studentID = studentID;
        // current = null;
        try {
            // System.out.println("Current registered courses:");
            Label title = new Label("Current registered courses for student " + studentID + ":");
            TableView<Course> currentSubject = new TableView<>();
            ObservableList<Course> currentList = FXCollections.observableArrayList();
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] studentParts = line.split(",");
                if (studentParts.length > 0 && studentID.equals(studentParts[0])) {
                    for (int i = 3; i < studentParts.length; i++) {
                        String currentCourse = studentParts[i].trim();
                        // System.out.println(currentCourse);
                        currentList.add(new Course(currentCourse));
                    }
                    break; // No need to continue searching once we've found the student's courses
                }

            }
            Button backButton = new Button("Back");
            TableColumn<Course, String> column2 = new TableColumn<>("Course");
            column2.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            currentSubject.getColumns().add(column2);
            currentSubject.setItems(currentList);
            currentSubject.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

            getChildren().clear();
            getChildren().addAll(title, currentSubject, backButton);
            backButton.setOnAction(e -> {
                StudentDashboardFx studentDashboard = new StudentDashboardFx(studentID);
                studentDashboard.start(primaryStage);
                return;
            });
            setAlignment(Pos.CENTER);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Displays the courses that the student can enroll in the future.
     * 
     * @param studentId The ID of the student.
     */
    public void futureCourse(String studentId) {
        this.studentID = studentId;
        try {

            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            List<String> passRegisterLines = Files.readAllLines(Paths.get("passRegister.csv"));
            TableView<Course> futureSubject = new TableView<>();
            ObservableList<Course> futureList = FXCollections.observableArrayList();
            List<String> courseLines = Files.readAllLines(Paths.get("course.csv"));
            TreeSet<String> futureCourseSet = new TreeSet<>(); // Use a set to avoid duplicate course codes

            for (String course : courseLines) {
                String[] items = course.split(",");
                futureCourseSet.add(items[1]);
            }

            // Remove courses from futureCourseSet that are found in student.csv
            for (String line : studentLines) {
                String[] parts = line.split(",");
                // Check if the first part (student ID) matches the given studentId
                if (parts.length > 0 && parts[0].equals(studentId)) {
                    for (int i = 3; i < parts.length; i++) {
                        String studentCourse = parts[i];
                        futureCourseSet.remove(studentCourse);
                    }
                }
            }
            // Remove courses from futureCourseSet that are found in student.csv
            for (String pass : passRegisterLines) {
                String[] item = pass.split(",");
                // Check if the first part (student ID) matches the given studentId
                if (item.length > 1 && item[1].equals(studentId)) {
                    for (int i = 4; i < item.length; i++) {
                        String studentCourse = item[i];
                        futureCourseSet.remove(studentCourse);
                    }
                }
            }

            // Print the future course codes for the student
            for (String course : futureCourseSet) {
                futureList.add(new Course(course));
            }
            Button backButton = new Button("Back");
            Label title = new Label("Future courses for student " + studentId + ":");
            TableColumn<Course, String> column3 = new TableColumn<>("Course");
            column3.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
            futureSubject.getColumns().add(column3);
            futureSubject.setItems(futureList);
            futureSubject.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

            getChildren().clear();
            getChildren().addAll(title, futureSubject, backButton);
            backButton.setOnAction(e -> {
                StudentDashboardFx studentDashboard = new StudentDashboardFx(studentID);
                studentDashboard.start(primaryStage);
                return;
            });
            setAlignment(Pos.CENTER);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
