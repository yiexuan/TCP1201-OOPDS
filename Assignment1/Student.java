import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Student extends User {
    Set<String> registeredCourses = new LinkedHashSet<>();

    public Student(String userID, String password) {
        super(userID, password);
        this.registeredCourses = new LinkedHashSet<>();
    }

    void registerCourse(Set<Course> courses) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("List of Course Code:");
        for (Course course : courses) {
            System.out.println(course.courseCode);
        }
        while (true) {
            System.out.print("Please provide course that you want to register: ");
            String selectedCourseCode = scanner.next().toUpperCase();

            Course selectedCourse = findCourse(courses, selectedCourseCode);

            if (selectedCourse != null) {
                boolean studentAlreadyRegistered = registeredCourses.contains(selectedCourseCode);
                if (!studentAlreadyRegistered) {
                    registeredCourses.add(selectedCourseCode);
                    studentAlreadyRegistered = selectedCourse.studentsEnrolled.contains(this);
                    // studentAlreadyRegistered = selectedCourse.assignedLecturer.studentsInCourse.contains(this);
                    // if (!studentAlreadyRegistered)
                    //     selectedCourse.assignedLecturer.studentsInCourse.add(this);
                    selectedCourse.studentsEnrolled.add(this);
                    addCourseToStudentFile(this.userID, selectedCourse);
                    System.out.println("Course registered successfully.");
                    System.out.println();
                    break;
                } else {
                    System.out.println("Student already registered for the course " + selectedCourseCode + ".");
                    System.out.println();
                    break;
                }

            } else {
                System.out.println("Invalid course selection.Please try again.");
                System.out.println();
                continue;
            }
        }

    }

    public void viewRegisteredCourses() {
        System.out.println("Registered Course:");
        for (String course : registeredCourses) {
            System.out.println(course);
        }
        System.out.println();
    }

    public Course findCourse(Set<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    public static void readStudentFile() {
        try {

            List<String> lines = Files.readAllLines(Paths.get("user.csv"));
            List<String> firstColumn = new ArrayList<>();
            for (String line : lines) {
                String[] items = line.split(",");
                firstColumn.add(items[0]);
            }
            Files.write(Paths.get("student.csv"), firstColumn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void addCourseToStudentFile(String studentId, Course course) {
        StringBuilder sb = new StringBuilder();
        // loop through the file to find the studentId
        // if found, append the course to the studentId
        // if not found, append the studentId and the course to the file
        

        // Path path = Paths.get("student.csv");
        // try {
        //     List<String> lines = Files.readAllLines(path);
        //     List<String> modifiedLines = new ArrayList<>();
        //     for (String line : lines) {
        //         String[] items = line.split(",");
        //         if (items[0].equals(studentId)) {
        //             line += "," + String.join(",", newCourses);
        //         }
        //         modifiedLines.add(line);
        //     }
        //     Files.write(path, modifiedLines);
        // } catch (IOException ex) {
        //     System.out.println(ex.getMessage());
        // }
    }

    // private static void saveStudentToFile(Student student) {
    // StringBuilder sb = new StringBuilder();
    // sb.append(student.userID).append(",\n");
    // try {
    //     Files.write(Paths.get("student.csv"), sb.toString().getBytes(), StandardOpenOption.APPEND);
    // } catch (IOException ex) {
    //     System.out.println(ex.getMessage());
    // }
}
