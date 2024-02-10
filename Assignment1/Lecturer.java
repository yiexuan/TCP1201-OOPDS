import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Lecturer extends User {
    List<Student> studentsInCourse;

    public Lecturer(String userID, String password) {
        super(userID, password);
        this.studentsInCourse = new ArrayList<>();
    }

    @Override
    public String toString() {
        return userID; // Assuming userID is the unique identifier for a Lecturer
    }
    
    public void viewStudentsInCourses(Set<Course> courses) {
        Scanner scanner = new Scanner(System.in);
        while(true){
        System.out.println("Courses Assigned to " + this.userID + ":");
        boolean assignedToCourse = false;
       
        for (Course course : courses) {
            if (course.assignedLecturer.equals(this)) {
                assignedToCourse = true;
                System.out.println(course.courseCode);
            }
        }

        if (!assignedToCourse) {
            System.out.println("No Assigned Courses.\n");
            return;
        }

        System.out.print("Enter the Course Code to view students: ");
        String courseCode = scanner.next().toUpperCase();

        Course course = findCourse(courses, courseCode);

        if (course != null) {
            System.out.println("Student List for Course " + courseCode + ":");
                if (course.assignedLecturer.equals(this)) {
                    for (Student student : this.studentsInCourse) {
                        if (student.registeredCourses.contains(courseCode)) {
                            System.out.println(student.userID);
                        }
                    }
                    System.out.println();
                    return;
                }
            System.out.println();
        } 
        else {
            System.out.println("Course not found or not assigned to you.\n");
            continue;
        }
        }
    }

    public Course findCourse(Set<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

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
            System.out.println("Course added successfully for lecturer: " + lecturerId);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
