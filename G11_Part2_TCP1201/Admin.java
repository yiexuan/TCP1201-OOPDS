/**
 * Admin class is a subclass of User class. 
 * It is used to create an Admin object with the given userID and password.
 */
public class Admin extends User {
     /**
     * Constructs a new Admin object with the specified userID and password.
     * 
     * @param userID userID of the admin to login
     * @param password password of the admin to login
     */
    public Admin(String userID, String password) {
        super(userID, password);
    }
}
