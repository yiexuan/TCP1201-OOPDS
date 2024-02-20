/**
 * The User class represents a generic user in a system with a userID and
 * password.
 * It provides methods to create a user object and convert it to a CSV string
 * format.
 */
public class User {
    /** The unique identifier for the user. */
    String userID;

    /** The password associated with the user. */
    String password;

    /**
     * Constructs a new User object with the specified userID and password.
     *
     * @param userID   the unique identifier for the user
     * @param password the password associated with the user
     */
    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

     /**
     * Converts the User object to a CSV string format.
     * The format of the CSV string is: userID,password,userType
     * where userType can be "user", "student", or "lecturer" based on the type of user.
     *
     * @return the CSV representation of the User object
     */
    public String toCSVString() {
        String userType = "user";
        if (this instanceof Student) {
            userType = "student";
        } else if (this instanceof Lecturer) {
            userType = "lecturer";
        }
        return userID + "," + password + "," + userType;
    }

       /**
     * Returns the userID.
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }
}
