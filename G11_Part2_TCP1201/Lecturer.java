
import java.util.ArrayList;
import java.util.List;

/**
 * The Lecturer class represents a lecturer in the 1-year certificate program.
 * Each lecturer has a list of students enrolled in the course they are
 * teaching.
 */
public class Lecturer extends User {

    /** The name for the user. */
    public String name;

    /** The list of students in the course */
    List<Student> studentsInCourse = new ArrayList<>();

    /**
     * Constructs a new Lecturer object with the specified userID and password.
     * 
     * @param userID   userID of the lecturer
     * @param password password of the lecturer to login
     */
    public Lecturer(String userID, String password) {
        super(userID, password);
        this.studentsInCourse = new ArrayList<>();
    }

    /**
     * Constructs a new Lecturer object with the specified userID, name and
     * password.
     * 
     * @param userID   userID of the lecturer
     * @param name     name of the lecturer
     * @param password password of the lecturer to login
     */
    public Lecturer(String userID, String name, String password) {
        super(userID, password);
        this.name = name;
        this.studentsInCourse = new ArrayList<>();
    }

    /**
     * Returns the string representation of the userID of the Lecturer object.
     * 
     * @return the userID of the Lecturer object
     */
    @Override
    public String toString() {
        return userID;
    }

    /**
     * Returns the userID of the Lecturer object.
     * 
     * @return the userID of the Lecturer object
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Returns the name of the Lecturer object.
     * 
     * @return the name of the Lecturer object
     */
    public String getName() {
        return name;
    }
}
