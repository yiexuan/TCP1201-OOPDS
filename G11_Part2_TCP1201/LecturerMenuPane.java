import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * The LecturerMenuPane class represents the user interface for the lecturer
 * menu.
 * It extends VBox to organize its contents vertically.
 */
public class LecturerMenuPane extends VBox {
    private Stage primaryStage;
    private Scene lecturerMenuScene;
    private Set<Course> courses;
    private String lecturerID;

    /**
     * Constructs a LecturerMenuPane with the specified primary stage and lecturer
     * ID.
     *
     * @param primaryStage The primary stage of the application.
     * @param userID       The ID of the lecturer.
     */
    public LecturerMenuPane(Stage primaryStage, String userID) {
        this.primaryStage = primaryStage;
        this.courses = CourseUtil.readCourseFromCourseFile();
        Button viewStudentButton = new Button("View Student in Courses");
        viewStudentButton.setOnAction(e -> viewStudentsInCourses(userID));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());

        setAlignment(Pos.CENTER);
        getChildren().addAll(viewStudentButton, logoutButton);
    }

    /**
     * Sets the scene associated with this pane.
     *
     * @param scene The scene to be set.
     */
    public void setScene(Scene scene) {
        this.lecturerMenuScene = scene;
    }

    /**
     * Displays the students enrolled in the courses taught by the lecturer.
     *
     * @param userID The ID of the lecturer.
     */
    private void viewStudentsInCourses(String userID) {
        this.lecturerID = userID;
        Button backtoMainButton = new Button("Back");
        TableView<Student> stuTableView = new TableView<>();
        ChoiceDialog<Course> courseDialog = new ChoiceDialog<>(null, courses);
        courseDialog.setTitle("Select Course");
        courseDialog.setHeaderText("List of Courses");
        courseDialog.setContentText("Select a Course:");
        ButtonType viewButton = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType backButton = new ButtonType("Back", ButtonData.CANCEL_CLOSE);
        courseDialog.getDialogPane().getButtonTypes().clear();
        courseDialog.getDialogPane().getButtonTypes().setAll(viewButton, backButton);

        List<Course> lecturerCourses = courses.stream()
                .filter(course -> course.getAssignedLecturer() != null
                        && course.getAssignedLecturer().getUserID().equals(this.lecturerID))
                .collect(Collectors.toList());

        courseDialog.getItems().clear(); // Clear existing items
        courseDialog.getItems().addAll(lecturerCourses); // Add filtered courses
        courseDialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            return;
        });

        Optional<Course> courseResult = courseDialog.showAndWait();
        if (courseResult.isEmpty()) {
            return;
        }
        courseResult.ifPresent(selectedCourse -> {
            TableColumn<Student, String> stuUserIDCol = new TableColumn<>("Student ID");
            // stuUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
            stuUserIDCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserID()));
            stuTableView.getColumns().add(stuUserIDCol);

            TableColumn<Student, String> stuNameCol = new TableColumn<>("Student Name");
            stuNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            stuTableView.getColumns().add(stuNameCol);

            if (selectedCourse != null) {
                ObservableList<Student> stuData = FXCollections
                        .observableArrayList(selectedCourse.getStudentsEnrolled());
                stuTableView.setItems(stuData);
                stuTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
            }

            getChildren().clear();
            Label title = new Label("Students for course " + selectedCourse);
            getChildren().addAll(title, stuTableView, backtoMainButton);
            backtoMainButton.setOnAction(e -> {
                LecturerDashboardFx lecturerDashboard = new LecturerDashboardFx(userID);
                lecturerDashboard.start(primaryStage);
                // primaryStage.setScene(lecturerMenuScene);
                return;
            });
            setAlignment(Pos.CENTER);

        });

    }

    /**
     * Handles the logout action.
     */
    private void handleLogout() {
        Logout logout = new Logout(primaryStage);
        logout.confirmLogout();
    }
}
