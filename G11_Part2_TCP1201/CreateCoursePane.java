import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The CreateCoursePane class represents the graphical user interface for
 * creating a new course.
 * It extends GridPane to organize its contents in a grid layout.
 */
public class CreateCoursePane extends GridPane {

    private TextField courseCodeField;
    private TextField courseCreditField;
    private TextField prerequitesField;
    private Button createButton;
    private Scene adminMenuScene;
    private Stage primaryStage;

    /**
     * Constructs a CreateCoursePane with the specified primary stage and admin menu
     * scene.
     *
     * @param primaryStage   The primary stage of the application.
     * @param adminMenuScene The scene of the admin menu.
     */
    public CreateCoursePane(Stage primaryStage, Scene adminMenuScene) {
        this.adminMenuScene = adminMenuScene;
        this.primaryStage = primaryStage;

        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);
        setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            AdminDashboardFx adminDashboard = new AdminDashboardFx();
            adminDashboard.start(primaryStage);
        });

        Label courseCodeLabel = new Label("New Course Code:");
        add(courseCodeLabel, 0, 0);
        courseCodeField = new TextField();
        add(courseCodeField, 1, 0);
        courseCodeField.setPrefWidth(200);

        Label creditLabel = new Label("Course Credit:");
        add(creditLabel, 0, 1);
        courseCreditField = new TextField();
        add(courseCreditField, 1, 1);

        Label prerequisitesLabel = new Label("Prerequisites(comma-separated):");
        add(prerequisitesLabel, 0, 2);
        prerequitesField = new TextField();
        prerequitesField.setPromptText("Enter 'Nil' if no prerequisite");
        add(prerequitesField, 1, 2);

        createButton = new Button("Create Course");
        createButton.setOnAction(e -> createCourse());

        add(createButton, 1, 3);
        add(backButton, 1, 4);
        setPrefSize(600, 400);
    }

    /**
     * Creates a new course based on the user input.
     */
    private void createCourse() {
        String courseCode = courseCodeField.getText().toUpperCase();
        String courseCreditStr = courseCreditField.getText();
        String prerequisites = prerequitesField.getText();

        if (courseCode.isEmpty() || courseCreditStr.isEmpty() || prerequisites.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        int courseCredit = Integer.parseInt(courseCreditStr);
        Course newCourse = new Course(courseCredit, courseCode, prerequisites);
        if (CourseUtil.createCourse(courseCode, newCourse)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Course created successfully");
            alert.setContentText("Do you want to create a new course?");

            ButtonType createAnotherButton = new ButtonType("Yes");
            ButtonType mainMenuButton = new ButtonType("Main Menu");

            alert.getButtonTypes().setAll(createAnotherButton, mainMenuButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == createAnotherButton) {
                    clearFields();
                } else if (response == mainMenuButton) {
                    AdminDashboardFx adminDashboard = new AdminDashboardFx();
                    adminDashboard.start(primaryStage);
                }
            });
        } else
            clearFields();

    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        courseCodeField.clear();
        courseCreditField.clear();
        prerequitesField.clear();
    }

}
