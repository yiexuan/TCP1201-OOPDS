import java.util.ArrayList;
import java.util.List;

public class Course {
    String courseCode;
    int credits;
    String assignedLecturer;
    List<String> prerequisites;
    List<Lecturer> assignedLecturers;
    List<Student> studentsEnrolled;

    public Course(String courseCode, int credits, List<String> prerequisites) {
        this.courseCode = courseCode;
        this.credits = credits;
        this.prerequisites = prerequisites;
        this.assignedLecturers = new ArrayList<>();
        this.studentsEnrolled = new ArrayList<>();
    }
}

