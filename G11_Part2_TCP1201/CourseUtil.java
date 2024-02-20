import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The CourseUtil class provides methods related to creating and managing Course objects.
 * It also provides methods to read courses and write course data to and from the course.csv file.
 */
public class CourseUtil {
  
    /**
     * Constructs a new CourseUtil object.
     */
    private CourseUtil() {
    };

    /**
     * Reads the course.csv file and returns a set of Course objects.
     * @return a set of Course objects
     */
    public static Set<Course> readCourseFromCourseFile() {
        Set<Course> courses = new LinkedHashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("course.csv"));
            for (String line : lines) {
                // String[] items;
                // items = line.split(",");
                String[] items = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                int credits = Integer.parseInt(items[0]);
                String courseCode = items[1];

                String tempPrerequisites = items[2];

                List<Student> studentsEnrolled = new ArrayList<>();
                Course course = new Course(credits, courseCode, tempPrerequisites);
                courses.add(course);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return courses;
    }

    // public Set<Course> getCourses() {
    //     return courses;
    // }

    /**
     * Checks if the given course code upon creation is already taken.
     * @param courseCode the course code input by the user
     * @param courses the set of courses to check
     * @return true if the course code is taken, false otherwise
     */
    private static boolean isCourseCodeTaken(String courseCode, Set<Course> courses) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new course object to the course.csv file.
     * @param course the course to add
     */
    private static void addCourseToCourseFile(Course course) {
        StringBuilder sb = new StringBuilder();
        String temp = "";
        temp = course.prerequisites;
        course.prerequisites = "\"" + temp + "\"";

        sb.append(course.toCSVString()).append("\n");
        try {
            Files.write(Paths.get("course.csv"), sb.toString().getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Adds a course to specific row in the lecturer.csv file based on the lecturer ID,
     * to indicate that the course is assigned to the lecturer.
     * @param lecturerId the lecturer ID to add the course to
     * @param course the course to add
     */
    public static void addCourseToLecturerFile(String lecturerId, Course course) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("lecturer.csv"));
            List<String> updatedLines = new ArrayList<>();

            // Iterate through each line in the file
            for (String line : lines) {
                String[] parts = line.split(","); // Split the line by comma

                // Check if the first part (lecturer ID) matches the given lecturerId
                if (parts.length > 0 && parts[0].equals(lecturerId)) {
                    // Append the course code to the current line
                    StringBuilder updatedLine = new StringBuilder(line);
                    updatedLine.append(course.courseCode).append(",");
                    updatedLines.add(updatedLine.toString());
                } else {
                    updatedLines.add(line); // Keep the line unchanged
                }
            }

            // Write the updated content back to the file
            Files.write(Paths.get("lecturer.csv"), updatedLines);
            // System.out.println("Course added successfully for lecturer: " + lecturerId);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Check if the course code is already taken upon creating a new course,
     * and adds the course to the course.csv file if the course code is not taken.
     * @param newCourseCode the course code input by the user
     * @param newCourse the course to add
     * @return true if the course is added successfully, false otherwise
     */
    public static boolean createCourse(String newCourseCode, Course newCourse) {
        if (isCourseCodeTaken(newCourseCode, readCourseFromCourseFile())) {
            codeTakenAlert();
            return false;
        } else {
            addCourseToCourseFile(newCourse);
            return true;
        }
    }

    /**
     * Displays alert message if the course code is already taken.
     */
    private static void codeTakenAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Course code already exists");
        alert.setContentText("Please input a different code.");
        alert.showAndWait();
        return;
    }
}
