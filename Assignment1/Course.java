import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Course {
    int credits;
    String courseCode;
    String prerequisites;
    Lecturer assignedLecturer = getAssignedLecturer();
    List<Student> studentsEnrolled = getStudentsEnrolled();

    public Course(int credits, String courseCode, String prerequisites) {
        this.credits = credits;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
        this.assignedLecturer = getAssignedLecturer();
        this.studentsEnrolled = getStudentsEnrolled();
    }

    public String toCSVString() {
        // String lecturerID = (assignedLecturer != null) ? assignedLecturer.userID :
        // "";
        // String prerequisitesString = String.join(",", prerequisites);
        // // Enclose prerequisites in double quotes
        // if (!prerequisitesString.isEmpty()) {
        // prerequisitesString = "\"" + prerequisitesString + "\"";
        // }
        return credits + "," + courseCode + "," + prerequisites;
    }

    public Lecturer getAssignedLecturer() {
        assignedLecturer = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get("lecturer.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                for (int i = 1; i < items.length; i++) {
                    if (items[i].equals(this.courseCode)) {
                        //String str = items[0];
                        assignedLecturer = new Lecturer(items[0], null);
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return assignedLecturer;
    }

    public List<Student> getStudentsEnrolled() {
        studentsEnrolled = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                for (int i = 2; i < items.length; i++) {
                    if (items[i].equals(this.courseCode))
                        this.studentsEnrolled.add(new Student(items[0], null));
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return this.studentsEnrolled;
    }
}