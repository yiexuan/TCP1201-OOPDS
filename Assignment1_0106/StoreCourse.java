import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StoreCourse {
    private static void writeCoursesToFile(List<Course> courses, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Course course : courses) {
                StringBuilder line = new StringBuilder();
                line.append(course.credits).append(", ").append(course.courseCode).append(", ");
                for (String prerequisite : course.prerequisites) {
                    line.append(prerequisite).append(", ");
                }
                // Remove the trailing comma and space
                line.setLength(line.length() - 2);
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
