/**
 * The Student class represents a student in the 1-year certificate program.
 */
public class Student extends User {
    /** The unique identifier for the user. */
    public String userID;
    /** The name for the user. */
    public String name;
    
    /**
     * Constructs a new Student object with the specified userID and password.
     * @param userID userID of the student 
     * @param password password of the student to login
     */
    public Student(String userID, String password) {
        super(userID, password);
        this.userID = userID; 
    }

    /**
     * Constructs a new Student object with the specified userID, name, and password.
     * @param userID userID of the student
     * @param name name of the student
     * @param password password of the student to login
     */
    public Student(String userID, String name, String password) {
        super(userID, password);
        this.userID = userID; 
        this.name = name; 
    }

    /**
     * Returns the userID of the Student object.
     * @return the userID of the Student
     */
    public String getUserID() {
        return this.userID;
    }
     
    /**
     * Returns the name of the Student object.
     * @return the name of the Student
     */
    public String getName() {
        return this.name;
    }
}
