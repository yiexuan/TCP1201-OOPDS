import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The ViewForCourse class represents a graphical user interface component
 * for viewing students and lecturers associated with a selected course.
 * It extends the VBox class and provides functionalities to display
 * student and lecturer information in table views.
 */
public class ViewForCourse extends VBox {
    Button backButton = new Button("Back");
    TableView<Student> stuTableView = new TableView<>();
    TableView<Lecturer> lecTableView = new TableView<>();

    /**
     * Constructs a new ViewForCourse instance with the specified courses,
     * lecturers,
     * students, and primary stage.
     *
     * @param courses      the set of courses to choose from
     * @param lecturers    the map of lecturer IDs to lecturer objects
     * @param students     the map of student IDs to student objects
     * @param primaryStage the primary stage of the application
     */
    public ViewForCourse(Set<Course> courses,
            Map<String, Lecturer> lecturers, Map<String, Student> students, Stage primaryStage) {
        ChoiceDialog<Course> courseDialog = new ChoiceDialog<>(null, courses);
        courseDialog.setTitle("Select Course");
        courseDialog.setHeaderText("List of Courses");
        courseDialog.setContentText("Select a Course:");
        ButtonType AssignButton = new ButtonType("OK", ButtonData.OK_DONE);
        courseDialog.getDialogPane().getButtonTypes().clear();
        courseDialog.getDialogPane().getButtonTypes().setAll(AssignButton);
        courseDialog.showAndWait().ifPresent(selectedCourse -> {

            // Create columns for student table
            TableColumn<Student, String> stuUserIDCol = new TableColumn<>("Student ID");
            stuUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

            TableColumn<Student, String> stuNameCol = new TableColumn<>("Student Name");
            stuNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

            // Create columns for lecturer table
            TableColumn<Lecturer, String> lecUserIDCol = new TableColumn<>("Lecturer ID");
            lecUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

            TableColumn<Lecturer, String> lecNameCol = new TableColumn<>("Lecturer Name");
            lecNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

            // Add columns to tables
            stuTableView.getColumns().add(stuUserIDCol);
            stuTableView.getColumns().add(stuNameCol);
            lecTableView.getColumns().add(lecUserIDCol);
            lecTableView.getColumns().add(lecNameCol);

            if (selectedCourse != null) {
                ObservableList<Student> stuData = FXCollections
                        .observableArrayList(selectedCourse.getStudentsEnrolled());
                ObservableList<Lecturer> lecData = FXCollections
                        .observableArrayList(selectedCourse.getAssignedLecturer());
                stuTableView.setItems(stuData);
                lecTableView.setItems(lecData);
                stuTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
                lecTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
            }

            Label stuTitle = new Label("Students for course " + selectedCourse);
            Label lecTitle = new Label("Lecturer for course " + selectedCourse);
            backButton.setOnAction(e -> {
                AdminDashboardFx adminDashboard = new AdminDashboardFx();
                adminDashboard.start(primaryStage);
            });
            getChildren().addAll(stuTitle, stuTableView, lecTitle, lecTableView, backButton);
            setAlignment(Pos.CENTER);
            setPrefSize(600, 400);
        });
 
    }
}
