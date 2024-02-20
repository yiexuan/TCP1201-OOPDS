import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Lecturer extends User {
    List<Student> studentsInCourse = new ArrayList<>();

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
            if (course.assignedLecturer != null && course.assignedLecturer.userID.equals(this.userID)) {
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
        studentsInCourse = getStudentsInCourse(courseCode);
        Course course = findCourse(courses, courseCode);

        if (course != null) {
            System.out.println("Student List for Course " + courseCode + ":");
                if (course.assignedLecturer.userID.equals(this.userID)) {
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

    public static Lecturer fromString(String str) {
        String[] parts = str.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid string format for Lecturer: " + str);
        }
        String userID = parts[0].trim();
        String name = parts[1].trim();
        return new Lecturer(userID, name);
    }

    public List<Student> getStudentsInCourse(String courseCode) {
        studentsInCourse = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                for (int i = 2; i < items.length; i++) {
                    if (items[i].equals(courseCode))
                        studentsInCourse.add(new Student(items[0], null));
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return studentsInCourse;
    }
}

