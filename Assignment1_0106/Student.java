import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Student extends User {
    List<String> registeredCourses;

    public Student(String userID, String password) {
        super(userID, password);
        this.registeredCourses = new ArrayList<>();
    }

    void registerCourse(List<Course> courses) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("List of Course Code:");
        for (Course course : courses) {
            System.out.println(course.courseCode);
        }
        System.out.print("Please provide course that you want to register: ");
        String selectedCourseCode = scanner.next().toUpperCase();

        Course selectedCourse = findCourse(courses, selectedCourseCode);

        if (selectedCourse != null){        //&& !registeredCourses.contains(selectedCourseCode)) {
            boolean studentAlreadyRegistered = registeredCourses.contains(selectedCourseCode);
            if(!studentAlreadyRegistered){
                registeredCourses.add(selectedCourseCode);
                for (Lecturer assignedLecturer : selectedCourse.assignedLecturers) {                 
                    studentAlreadyRegistered = assignedLecturer.studentsInCourse.contains(this);
                    if (!studentAlreadyRegistered) {
                        assignedLecturer.studentsInCourse.add(this);
                    }
                }
                selectedCourse.studentsEnrolled.add(this);
                System.out.println("Course registered successfully.");
            }
            else
                System.out.println("Student already registered");
            System.out.println();
        } 
        else {
            System.out.println("Invalid course selection");
            System.out.println();
        }
    }

    public void viewRegisteredCourses() {
        System.out.println("Registered Course:");
        for (String course : registeredCourses) {
            System.out.println(course);
        }
        System.out.println();
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
