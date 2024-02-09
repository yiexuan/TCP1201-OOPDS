import java.util.List;
import java.util.Scanner;
import java.util.Set;
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
                    studentAlreadyRegistered = selectedCourse.assignedLecturer.studentsInCourse.contains(this);                    
                    if (!studentAlreadyRegistered) 
                        selectedCourse.assignedLecturer.studentsInCourse.add(this);
                    selectedCourse.studentsEnrolled.add(this);
                    System.out.println("Course registered successfully.");
                    System.out.println();
                    break;
                } 
                else {
                    System.out.println("Student already registered for the course "+selectedCourseCode+".");
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
}