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
    Lecturer assignedLecturer;
    List<Student> studentsEnrolled = new ArrayList<>();

    public Course(int credits, String courseCode, String prerequisites) {
        this.credits = credits;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
        this.studentsEnrolled = new ArrayList<>();
    }
    public String toCSVString() {
        String lecturerID = (assignedLecturer != null) ? assignedLecturer.userID : "";
        // String prerequisitesString = String.join(",", prerequisites);
        // // Enclose prerequisites in double quotes
        // if (!prerequisitesString.isEmpty()) {
        //     prerequisitesString = "\"" + prerequisitesString + "\"";
        // }
        return credits + "," + courseCode + "," + prerequisites + "," + lecturerID;
    }
}

