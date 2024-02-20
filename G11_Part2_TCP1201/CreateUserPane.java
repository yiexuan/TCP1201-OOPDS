
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The CreateUserPane class represents the graphical user interface for creating
 * a new user (either a student or a lecturer).
 * It extends GridPane to organize its contents in a grid layout.
 */
public class CreateUserPane extends GridPane {
    private TextField userIDField;
    private PasswordField passwordField;
    private TextField nameField;
    private Button createButton;
    private Scene adminMenuScene;
    private Stage primaryStage;

     /**
     * Constructs a CreateUserPane with the specified user type, primary stage, and
     * admin menu scene.
     *
     * @param userType       The type of user to create (e.g., "Student" or
     *                       "Lecturer").
     * @param primaryStage   The primary stage of the application.
     * @param adminMenuScene The scene of the admin menu.
     */
    public CreateUserPane(String userType, Stage primaryStage, Scene adminMenuScene) {
        this.adminMenuScene = adminMenuScene;
        this.primaryStage = primaryStage;
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            AdminDashboardFx adminDashboard = new AdminDashboardFx();
            adminDashboard.start(primaryStage);
        });
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);
        setAlignment(Pos.CENTER);

        Label userIDLabel = new Label("New " + userType + " ID:");
        add(userIDLabel, 0, 0);
        userIDField = new TextField();
        add(userIDField, 1, 0);

        Label passwordLabel = new Label("New Password:");
        add(passwordLabel, 0, 1);
        passwordField = new PasswordField();
        add(passwordField, 1, 1);

        Label nameLabel = new Label("New " + userType + " Name:");
        add(nameLabel, 0, 2);
        nameField = new TextField();
        add(nameField, 1, 2);

        createButton = new Button("Create " + userType);
        add(createButton, 1, 3);
        add(backButton, 1, 4);
        

        // Set action for createButton
        createButton.setOnAction(e -> createUser(userType));
    }

      /**
     * Creates a new user based on the user input.
     *
     * @param userType The type of user to create,either "Student" or "Lecturer".
     */
    private void createUser(String userType) {
        String userID = userIDField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();

        if (userID.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        UserUtil userUtil = new UserUtil();
        if (userType.equals("Student")) {
            if (userUtil.createStudent(userID, name, password)) {
                successfulCreateAlert(userType);
            } else
                clearFields();
            ;
        } else if (userType.equals("Lecturer")) {
            if (userUtil.createLecturer(userID, name, password)) {
                successfulCreateAlert(userType);
            } else
                clearFields();
            ;
        }

    }

       /**
     * Clears all input fields.
     */
    private void clearFields() {
        userIDField.clear();
        passwordField.clear();
        nameField.clear();
    }

     /**
     * Displays a success alert after successfully creating a user.
     * Allows the user to assign another course or go back to the main menu.
     *
     * @param userType The type of user created.
     */
    public void successfulCreateAlert(String userType) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("User created successfully");
        alert.setContentText("Do you want to create another " + userType + "?");

        ButtonType createAnotherButton = new ButtonType("Yes");
        ButtonType mainMenuButton = new ButtonType("Main Menu");

        alert.getButtonTypes().setAll(createAnotherButton, mainMenuButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == createAnotherButton) {
                userIDField.clear();
                passwordField.clear();
                nameField.clear();
            } else if (response == mainMenuButton) {
                AdminDashboardFx adminDashboard = new AdminDashboardFx();
                adminDashboard.start(primaryStage);
            }
        });
    }

}
