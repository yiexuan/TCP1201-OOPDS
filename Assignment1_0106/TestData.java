import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TestData {

    public static void createTestData(Admin admin, List<Student> students, List<Lecturer> lecturers,
            List<Course> courses) {
        // Course newCourse = new Course("CS113", 3, Arrays.asList("Nil"));
        // courses.add(newCourse);

        Lecturer newLecturer = new Lecturer("lec001", "lecpass");
        lecturers.add(newLecturer);

        Student newStudent = new Student("stu001", "stupass");
        students.add(newStudent);
    }
}
