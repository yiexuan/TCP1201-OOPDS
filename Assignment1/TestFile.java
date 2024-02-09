import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TestFile {
  static String filename = "course.csv";
  public static void main(String[] args) {
    System.out.println("Load students from file " + filename);
    ArrayList<Student> students = readCourseFromFile();
    for (Student s : students)
      System.out.println(s);
    System.out.println();

    // insert a new student
    Scanner input = new Scanner(System.in);
    System.out.print("Enter new student id  : ");
    int newId = input.nextInt();
    System.out.print("Enter new student name: ");
    input.nextLine(); // read away unwanted newline.
    String newName = input.nextLine(); // read user input.
    input.close();

    // check if id exist
    boolean idExist = false;
    for (Student s : students) {
      // reject id if it already exists
      if (newId == s.getId()) {
        System.out.println("Cannot add id " + newId + " because it already exists.");
        idExist = true;
        break;
      }
    }

    // save new student if id does not exist
    if (idExist == false) {
      students.add(new Student(newId, newName));
      System.out.println("Save students to file " + filename);
      saveStudentToFile(students);
    }
  }

  private static Set<Course> readCourseFromFile() {
    Set<Course> courses = new LinkedHashSet<>();
    try {
      // read students.csv into a list of lines.
      List<String> lines = Files.readAllLines(Paths.get(filename));
      for (int i = 0; i < lines.size(); i++) {
        // split a line by comma
        String[] items = lines.get(i).split(",");

        // items[0] is id, items[1] is name
        int credits = Integer.parseInt(items[0]); // convert String to int
        String courseCode = items[1];
        Set<String> prerequisites = new HashSet<>(Arrays.asList(items[2].split(",")));
        Lecturer assignedLecturer = lecturers.get(items[3]);
        List<Student> studentsEnrolled;
      }
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    return courses;
  }

  private static void saveStudentToFile(ArrayList<Student> students) {
    // read students.csv into a list of lines.
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < students.size(); i++)
      sb.append(students.get(i).toCSVString() + "\n");
    try {
      Files.write(Paths.get(filename), sb.toString().getBytes());
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }
}
