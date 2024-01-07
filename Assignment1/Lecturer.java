import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lecturer extends User {
    List<Student> studentsInCourse;

    public Lecturer(String userID, String password) {
        super(userID, password);
        this.studentsInCourse = new ArrayList<>();
    }

    public void viewStudentsInCourses(List<Course> courses) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Courses Assigned to " + this.userID + ":");
        boolean assignedToCourse = false;

        for (Course course : courses) {
            if (course.assignedLecturers.contains(this)) {
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
            for (Lecturer assignedLecturer : course.assignedLecturers) {
                if (assignedLecturer.equals(this)) {
                    for (Student student : assignedLecturer.studentsInCourse) {
                        if (student.registeredCourses.contains(courseCode)) {
                            System.out.println(student.userID);
                        }
                    }
                    System.out.println();
                    return;
                }
            }
            System.out.println();
        } 
        else {
            System.out.println("Course not found or not assigned to you.\n");
        }
    }

    public Course findCourse(List<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return course;
            }
        }
        return null;
    }
}
