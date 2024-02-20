
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The CMSFx class is the entry point of the Course Management System (CMS)
 * application.
 * It extends Application to provide the JavaFX application lifecycle methods.
 */
public class CMSFx extends Application {

    // Filename for the course data file
    static String courseFilename = "course.csv";

    // Admin instance with default credentials
    Admin admin = new Admin("admin", "adminpass");

    /**
     * The main method launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method initializes and displays the login scene.
     *
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create a LoginPane and set it as the root of the login scene
        LoginPane loginPane = new LoginPane(primaryStage);
        Scene loginScene = new Scene(loginPane, 600, 400);

        // Set the title and scene for the primary stage, then show it
        primaryStage.setTitle("Course Management System");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
}
