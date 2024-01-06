import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LoadCourse {
    public static void loadCourse(List<Course> courses) {
        String csvFile = "course.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].trim().isEmpty()) {
                    int courseCredit = Integer.parseInt(values[0].trim());
                    String courseCode = values[1].trim();
                    List<String> cs = new ArrayList<>();
                    for (int n = 2; n < values.length; n++) {
                        String[] prerequisitesArray = values[n].replaceAll("^\"|\"$", "").split(",");
                        for (String prerequisite : prerequisitesArray) {
                            // cs = Arrays.asList(prerequisite);
                            // System.out.println(prerequisite);
                            cs.add(prerequisite.trim());

                        }

                    }
                    Course course = new Course(courseCode, courseCredit, cs);
                    courses.add(course);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //test result
        for (Course course : courses) {
            System.out.println(course.courseCode + " " + course.credits + " " + course.prerequisites);

        }
    }
}
