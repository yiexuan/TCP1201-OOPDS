import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The UserUtil class provides methods related to creating and managing User objects.
 * It provides methods to read users and write user data to and from the user.csv file.
 */
public final class UserUtil {
    //public final static String userFilename = "user.csv";
    static Map<String, Student> students = readUsersFromFile("student");
    static Map<String, Lecturer> lecturers = readUsersFromFile("lecturer");

    /**
     * Constructs a new UserUtil object.
     */
    public UserUtil() {
    }

    /**
     * Returns the lecturers in map data structure.
     * @return a map of lecturer objects
     */
    public static Map<String, Lecturer> getLecturers() {
        return lecturers;
    }

    /**
     * Returns the students in map data structure.
     * @return a map of student objects
     */
    public static Map<String,Student>getStudents(){
        return students;
    }

    /**
     * Returns a map of User objects by reading from the user.csv file.
     * @param <E> the type of User object
     * @param userType the type of user to read only the specified type of user from the file
     * @return a map of specified type of User objects
     */
    static <E extends User> Map<String, E> readUsersFromFile(String userType) {
        Map<String, E> users = new LinkedHashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("user.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                String id = items[0];
                if (items[2].equals(userType)) {
                    if (userType.equals("student")) {
                        users.put(id, (E) new Student(id, items[1]));
                    } else if (userType.equals("lecturer")) {
                        users.put(id, (E) new Lecturer(id, items[1]));
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return users;
    }

    /**
     * Checks if the given userID and password match the user in the map, and 
     * returns a User object (either Student or Lecturer) if it matches.
     * @param userID the user ID to check
     * @param password the password of user to check
     * @param students the map of students
     * @param lecturers the map of lecturers
     * @return a User object if the user ID and password match the user in the map, null otherwise
     */
    public static User findUser(String userID, String password, Map<String, Student> students,
            Map<String, Lecturer> lecturers) {
        Student student = students.get(userID); // check if the ID exist in the map
        if (student != null && student.password.equals(password)) {
            return student;
        }

        Lecturer lecturer = lecturers.get(userID);
        if (lecturer != null && lecturer.password.equals(password)) {
            return lecturer;
        }
        return null;
    }

    /**
     * Saves the latest user data to the user.csv file after creating a new user.
     * @param students the map of students
     * @param lecturers the map of lecturers
     */
    private static void saveUserToFile(Map<String, Student> students, Map<String, Lecturer> lecturers) {
        Map<String, User> users = new LinkedHashMap<>();
        users.putAll(students);
        users.putAll(lecturers);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ? extends User> entry : users.entrySet()) {
            sb.append(entry.getValue().toCSVString()).append("\n");
        }
        try {
            Files.write(Paths.get("user.csv"), sb.toString().getBytes());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Saves the latest student data to the student.csv file after creating a new student.
     * @param student the new student to save
     */
    private static void saveStudentToFile(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append(student.getUserID() + "," + student.name + ",0,\n");
        try {
            Files.write(Paths.get("student.csv"), sb.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Saves the latest lecturer data to the lecturer.csv file after creating a new lecturer.
     * @param lecturer the new lecturer to save
     */
    private static void saveLecturerToFile(Lecturer lecturer) {
        StringBuilder sb = new StringBuilder();
        sb.append(lecturer.userID + "," + lecturer.name + ",\n");
        try {
            Files.write(Paths.get("lecturer.csv"), sb.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Checks if the given userID is already taken.
     * @param userID the user ID to check
     * @param students the map of students to check from
     * @param lecturers the map of lecturers to check from
     * @return true if the user ID is taken, false otherwise
     */
    private boolean isUserIDTaken(String userID, Map<String, Student> students, Map<String, Lecturer> lecturers) {
        return students.containsKey(userID) || lecturers.containsKey(userID) || userID.equals("admin");
    }

    /**
     * Returns true if the new student is created and saved to the student.csv file, false otherwise.
     * @param newStudentID the new student ID to create
     * @param name the name of the new student
     * @param password the password of the new student
     * @return true if the new student is created and saved, false otherwise
     */
    public Boolean createStudent(String newStudentID, String name, String password) {
        if (isUserIDTaken(newStudentID, students, lecturers)) {
            IDTakenAlert();
            return false;
        }
        Student newStudent = new Student(newStudentID, name, password);
        students.put(newStudentID, newStudent);
        saveUserToFile(students, lecturers);
        saveStudentToFile(newStudent);
        return true;
    }

    /**
     * Returns true if the new lecturer is created and saved to the lecturer.csv file, false otherwise.
     * @param newLecturerID the new lecturer ID to create
     * @param name the name of the new lecturer
     * @param password the password of the new lecturer
     * @return true if the new lecturer is created and saved, false otherwise
     */
    public Boolean createLecturer(String newLecturerID, String name, String password) {
        if (isUserIDTaken(newLecturerID, students, lecturers)) {
            IDTakenAlert();
            return false;
        }
        Lecturer newLecturer = new Lecturer(newLecturerID, name, password);
        lecturers.put(newLecturerID, newLecturer);
        saveUserToFile(students, lecturers);
        saveLecturerToFile(newLecturer);
        return true;
    }

    /**
     * Displays alert message if the user ID is taken.
     */
    public void IDTakenAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("The user ID has been taken");
        alert.setContentText("Please input another ID");
        alert.showAndWait();
        return;
    }
}
