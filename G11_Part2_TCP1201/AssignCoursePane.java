import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The AssignCoursePane class represents the graphical user interface for
 * assigning a course to a lecturer.
 * It extends VBox to organize its contents vertically.
 */
public class AssignCoursePane extends VBox {
    private Scene adminMenuScene;
    private Stage primaryStage;

    /**
     * Constructs an AssignCoursePane with the specified primary stage, admin menu
     * scene, set of courses, and map of lecturers.
     *
     * @param primaryStage   The primary stage of the application.
     * @param adminMenuScene The scene of the admin menu.
     * @param courses        The set of available courses.
     * @param lecturers      The map of available lecturers.
     */
    public AssignCoursePane(Stage primaryStage, Scene adminMenuScene, Set<Course> courses,
            Map<String, Lecturer> lecturers) {
        this.adminMenuScene = adminMenuScene;
        this.primaryStage = primaryStage;

        // Check if courses or lecturers are available
        if (courses.isEmpty() || lecturers.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("No courses or lecturers available.");
            alert.showAndWait();
            return;
        }

        // Dialog to select a course
        ChoiceDialog<Course> courseDialog = new ChoiceDialog<>(null, courses);
        courseDialog.setTitle("Select Course");
        courseDialog.setHeaderText("List of Courses");
        courseDialog.setContentText("Select a Course:");
        ButtonType AssignButton = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType backButton = new ButtonType("Back", ButtonData.CANCEL_CLOSE);
        courseDialog.getDialogPane().getButtonTypes().clear();
        courseDialog.getDialogPane().getButtonTypes().setAll(AssignButton, backButton);
        courseDialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
            return;
        });
        Optional<Course> courseResult = courseDialog.showAndWait();

        // Handle cancel or close action
        if (courseResult.isEmpty()) {
            primaryStage.close();
            AdminDashboardFx adminDashboard = new AdminDashboardFx();
            adminDashboard.start(new Stage());
        }

        // Proceed with course selection
        courseResult.ifPresent(selectedCourse -> {
            // Dialog to select a lecturer
            ChoiceDialog<Lecturer> lecturerDialog = new ChoiceDialog<>(null, lecturers.values());
            lecturerDialog.setTitle("Select Lecturer");
            lecturerDialog.setHeaderText("List of Lecturers");
            lecturerDialog.setContentText("Select a Lecturer:");
            lecturerDialog.getDialogPane().getButtonTypes().clear();
            lecturerDialog.getDialogPane().getButtonTypes().setAll(AssignButton, backButton);
            lecturerDialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
                return;
            });
            Optional<Lecturer> lecturerResult = lecturerDialog.showAndWait();

            if (lecturerResult.isEmpty()) {
                AssignCoursePane assignCoursePane = new AssignCoursePane(primaryStage, adminMenuScene,
                        courses, lecturers);
                primaryStage.setScene(new Scene(assignCoursePane));
            }

            // Proceed with lecturer selection
            lecturerResult.ifPresent(selectedLecturer -> {
                // Check if the course is already assigned to another lecturer
                if (selectedCourse.getAssignedLecturer() == null) {
                    // Assign the course to the selected lecturer if not being assigned yet
                    selectedCourse.setAssignedLecturer(selectedLecturer);
                    CourseUtil.addCourseToLecturerFile(selectedLecturer.getUserID(), selectedCourse);
                    // Show success alert
                    successfulAlert(primaryStage, adminMenuScene, courses, lecturers);
                } else {
                    // Show error alert if the course is already assigned
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("The course is already assigned to another lecturer.");
                    ButtonType okButton = new ButtonType("Ok");
                    errorAlert.getButtonTypes().setAll(okButton);
                    errorAlert.showAndWait().ifPresent(response -> {
                        AssignCoursePane assignCoursePane = new AssignCoursePane(primaryStage, adminMenuScene,
                                courses, lecturers);
                        primaryStage.setScene(new Scene(assignCoursePane));
                    });
                }
            });
        });
    }

    /**
     * Displays a success alert after assigning a course to a lecturer.
     * Allows the user to assign another course or go back to the main menu.
     *
     * @param primaryStage   The primary stage of the application.
     * @param adminMenuScene The scene of the admin menu.
     * @param courses        The set of available courses.
     * @param lecturers      The map of available lecturers.
     */
    private void successfulAlert(Stage primaryStage, Scene adminMenuScene, Set<Course> courses,
            Map<String, Lecturer> lecturers) {
        Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("A course has been assigned to the selected Lecturer."
                + "\n\nDo you want to assign another course or go back to the main menu?");
        ButtonType assignAnotherButton = new ButtonType("Assign Another");
        ButtonType mainMenuButton = new ButtonType("Main Menu");
        successAlert.getButtonTypes().setAll(assignAnotherButton, mainMenuButton);
        successAlert.showAndWait().ifPresent(response -> {
            if (response == assignAnotherButton) {
                AssignCoursePane assignCoursePane = new AssignCoursePane(primaryStage, adminMenuScene,
                        courses, lecturers);
                primaryStage.setScene(new Scene(assignCoursePane));
            } else if (response == mainMenuButton) {
                primaryStage.close();
                AdminDashboardFx adminDashboard = new AdminDashboardFx();
                adminDashboard.start(new Stage());
            }
        });
    }

}
