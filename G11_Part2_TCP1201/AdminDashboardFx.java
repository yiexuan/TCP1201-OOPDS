import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The AdminDashboardFx class represents the main entry point for the admin
 * dashboard application.
 * It extends the Application class provided by JavaFX.
 */
public class AdminDashboardFx extends Application {

    /**
     * The start method is the entry point for JavaFX applications.
     * It is called after the system is ready for the application to begin running.
     *
     * @param primaryStage The primary stage for this application, onto which the
     *                     application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        // Create an instance of AdminMenuPane
        AdminMenuPane adminMenuPane = new AdminMenuPane(primaryStage);
        // Create a scene with AdminMenuPane
        Scene adminMenuScene = new Scene(adminMenuPane, 600, 400);
        // Set the scene to the AdminMenuPane
        adminMenuPane.setScene(adminMenuScene);

        // Set up primary stage
        primaryStage.setTitle("Admin Dashboard"); // Set the title of the stage
        primaryStage.setScene(adminMenuScene); // Set the scene to the primary stage
        primaryStage.show(); // Show the primary stage
    }

    /**
     * Creates the admin menu scene with the specified primary stage.
     *
     * @param primaryStage The primary stage for the admin menu scene.
     * @return The created admin menu scene.
     */
    public Scene createAdminMenuScene(Stage primaryStage) {
        AdminMenuPane adminMenuPane = new AdminMenuPane(primaryStage);
        Scene adminMenuScene = new Scene(adminMenuPane, 600, 400);
        return adminMenuScene;
    }

    /**
     * The main method, which launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

}
