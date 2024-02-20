import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The LoginPane class represents the user interface for the login screen.
 * It extends GridPane to organize its contents in a grid.
 */
public class LoginPane extends GridPane {

    private TextField userIDField;
    private PasswordField passwordField;
    private Label messageLabel;
    private Map<String, Student> students;
    private Map<String, Lecturer> lecturers;
    private Stage primaryStage;

    
     /**
     * Constructs a LoginPane with the specified primary stage.
     *
     * @param primaryStage The primary stage of the application.
     */
    public LoginPane(Stage primaryStage) {
        this.students = UserUtil.readUsersFromFile("student");
        this.lecturers = UserUtil.readUsersFromFile("lecturer");
        setPadding(new Insets(20, 20, 20, 20));
        setVgap(10);
        setHgap(10);
        setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        

        Label userIDLabel = new Label("User ID:");
        gridPane.add(userIDLabel, 0, 0);
        userIDField = new TextField();
        gridPane.add(userIDField, 1, 0);

        Label passwordLabel = new Label("Password:");
        gridPane.add(passwordLabel, 0, 1);
        passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(primaryStage));
        gridPane.add(loginButton, 1, 2);

        messageLabel = new Label();
        gridPane.add(messageLabel, 1, 3);

        // Add gridPane to this GridPane
        add(gridPane, 0, 0);
    }

        /**
     * Handles the login process when the login button is clicked.
     *
     * @param primaryStage The primary stage of the application.
     */
    private void handleLogin(Stage primaryStage) {
        String userID = userIDField.getText();
        String password = passwordField.getText();

        if (userID.equals("admin") && password.equals("adminpass")) {
            messageLabel.setText("Admin login successful");
            AdminDashboardFx adminDashboard = new AdminDashboardFx();
            Scene adminMenuScene = adminDashboard.createAdminMenuScene(primaryStage); // Assuming you have a method in AdminDashboardFx to create the admin menu scene
            primaryStage.setScene(adminMenuScene);
   
        } else {
            User currentUser = UserUtil.findUser(userID, password, students, lecturers);
            if (currentUser != null) {
                if (currentUser instanceof Student) {
                    messageLabel.setText("Student login successful");
                    StudentDashboardFx studentDashboard = new StudentDashboardFx(userID);
                    Scene studentMenuScene = studentDashboard.createStudentMenuScene(primaryStage,userID);
                    primaryStage.setScene(studentMenuScene);

                } else if (currentUser instanceof Lecturer) {
                    messageLabel.setText("Lecturer login successful");
                    LecturerDashboardFx lecturerDashboard = new LecturerDashboardFx(userID);
                    Scene lecturerMenuScene = lecturerDashboard.createLecturerMenuScene(primaryStage,userID);
                    primaryStage.setScene(lecturerMenuScene);

                }
            } else {
                messageLabel.setText("Invalid credentials. Please try again.");
            }
        }
    }


}
