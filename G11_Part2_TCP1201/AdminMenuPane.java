import java.util.Map;
import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * The AdminMenuPane class represents the graphical user interface for the admin
 * menu.
 * It extends VBox to organize its contents vertically.
 */
public class AdminMenuPane extends VBox {
    private Stage primaryStage;
    private Scene adminMenuScene;
    private Set<Course> courses;
    private Map<String, Lecturer> lecturers;
    Map<String, Student> students;

    /**
     * Constructs an AdminMenuPane with the specified primary stage.
     *
     * @param primaryStage The primary stage of the application.
     */
    public AdminMenuPane(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.courses = CourseUtil.readCourseFromCourseFile();
        this.lecturers = UserUtil.getLecturers();
        this.students = UserUtil.getStudents();

        // Create buttons for related actions
        Button createStudentButton = new Button("Create Student");
        createStudentButton.setOnAction(e -> createNewUser("Student"));

        Button createLecturerButton = new Button("Create Lecturer");
        createLecturerButton.setOnAction(e -> createNewUser("Lecturer"));

        Button createCourseButton = new Button("Create Course");
        createCourseButton.setOnAction(e -> createCourse());

        Button assignCourseButton = new Button("Assign Course to Lecturer");
        assignCourseButton.setOnAction(e -> assignCourseToLecturer());

        Button viewStudentsLecturersButton = new Button("View Students and Lecturers for Course");
        viewStudentsLecturersButton.setOnAction(e -> viewStudentsAndLecturersForCourse());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        // Set alignment and add buttons to the layout
        setAlignment(Pos.CENTER);
        getChildren().addAll(createStudentButton, createLecturerButton, createCourseButton, assignCourseButton,
                viewStudentsLecturersButton, logoutButton);
    }

    /**
     * Sets the scene associated with this admin menu pane.
     *
     * @param scene The scene to set.
     */
    public void setScene(Scene scene) {
        this.adminMenuScene = scene;
    }

    /**
     * Creates a new user based on the specified user type.
     *
     * @param userType The type of user to create (Student or Lecturer).
     */
    private void createNewUser(String userType) {
        CreateUserPane createUserPane = new CreateUserPane(userType, primaryStage, adminMenuScene);
        // createUserPane.backButton.setOnAction(e->primaryStage.setScene(adminMenuScene));
        primaryStage.setScene(new Scene(createUserPane, 600, 400));
    }

    /**
     * Switches to the scene for creating a new course.
     */
    private void createCourse() {
        primaryStage.setScene(new Scene(new CreateCoursePane(primaryStage, adminMenuScene)));
    }

    /**
     * Switches to the scene for assigning a course to a lecturer.
     */
    private void assignCourseToLecturer() {
        primaryStage.setScene(new Scene(new AssignCoursePane(primaryStage, adminMenuScene, courses, lecturers)));
    }

    /**
     * Switches to the scene for viewing students enrolled and lecturers assigned to
     * courses.
     */
    private void viewStudentsAndLecturersForCourse() {
        primaryStage.setScene(new Scene(new ViewForCourse(courses, lecturers, students,primaryStage)));
    }

    /**
     * Handles the logout action by confirming and logging out the user.
     */
    private void handleLogout() {
        Logout logout = new Logout(primaryStage);
        logout.confirmLogout();
    }
}
