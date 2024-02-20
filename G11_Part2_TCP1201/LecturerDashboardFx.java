import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The LecturerDashboardFx class represents the main entry point for the
 * Lecturer Dashboard application.
 * It extends Application to provide the JavaFX application lifecycle methods.
 */
public class LecturerDashboardFx extends Application {
    public String userID;

    /**
     * Constructs a LecturerDashboardFx instance with the specified userID.
     *
     * @param userID The userID associated with the lecturer.
     */
    public LecturerDashboardFx(String userID) {
        this.userID = userID;
    }

    /**
     * The start method initializes and displays the lecturer menu scene.
     *
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create a LecturerMenuPane and set it as the root of the lecturer menu scene
        LecturerMenuPane lecturerMenuPane = new LecturerMenuPane(primaryStage, userID);
        Scene lecturerMenuScene = new Scene(lecturerMenuPane, 600, 400);
        lecturerMenuPane.setScene(lecturerMenuScene); // Set the scene after it's created

        // Set the title and scene for the primary stage
        primaryStage.setTitle("Lecturer Dashboard");
        primaryStage.setScene(lecturerMenuScene);
        primaryStage.show();

    }

    /**
     * Creates a new lecturer menu scene.
     *
     * @param primaryStage The primary stage of the application.
     * @param userID       The userID associated with the lecturer.
     * @return The newly created lecturer menu scene.
     */
    public Scene createLecturerMenuScene(Stage primaryStage, String userID) {
        LecturerMenuPane lecturerMenuPane = new LecturerMenuPane(primaryStage, userID);
        Scene lecturerMenuScene = new Scene(lecturerMenuPane, 600, 400);
        return lecturerMenuScene;
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
