import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * The Logout class represents the user interface for the logout screen in the Course Management System.
 * It provides a method to confirm the user's intention to logout.
 */
public class Logout {

    private Stage primaryStage;

    /**
     * Constructs a Logout pane with the specified primary stage.
     * @param primaryStage The primary stage of the application.
     */
    public Logout(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Displays a confirmation dialog to confirm the user's intention to logout.
     */
    public void confirmLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to logout, or Cancel to stay logged in.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Logout. Thank you for using the Course Management System.");
                logout();
            }
        });
    }

    /**
     * Logs the user out of the system.
     */
    private void logout() {
        // Set the scene back to the login scene
        CMSFx mainApp = new CMSFx();
        mainApp.start(primaryStage);
    }
}
