import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The StudentDashBoardFx class represents the main entry point for the
 * Student Dashboard application.
 * It extends Application to provide the JavaFX application lifecycle methods.
 */
public class StudentDashboardFx extends Application {
    private String userID;
    Scene studentMenuScene;

    /**
     * Constructs a StudentDashboardFx instance with the specified userID.
     *
     * @param userID The userID associated with the student.
     */
    public StudentDashboardFx(String userID) {
        this.userID = userID;
    }

    /**
     * The start method initializes and displays the student menu scene.
     *
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create a StudentMenuPane and set is as rhe root of the student menu scene
        StudentMenuPane studentMenuPane = new StudentMenuPane(primaryStage, userID);
        studentMenuScene = new Scene(studentMenuPane, 600, 400);
        studentMenuPane.setScene(studentMenuScene); // Set the scene after it's created

        // Set the title and scene for the primary stage
        primaryStage.setTitle("Student Dashboard");
        primaryStage.setScene(studentMenuScene);
        primaryStage.show();

    }

    /**
     * Creates a new student menu scene.
     *
     * @param primaryStage The primary stage of the application.
     * @param userID       The userID associated with the student.
     * @return The newly created student menu scene.
     */
    public Scene createStudentMenuScene(Stage primaryStage, String userID) {
        StudentMenuPane studentMenuPane = new StudentMenuPane(primaryStage, userID);
        Scene studentMenuScene = new Scene(studentMenuPane, 600, 400);
        studentMenuPane.setScene(studentMenuScene);
        return studentMenuScene;
    }

    /**
     * Retrieves the scene associated with the student menu.
     *
     * @return The scene of the student menu.
     */
    public Scene getScene() {
        return studentMenuScene;
    }

    /**
     * The main method launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
