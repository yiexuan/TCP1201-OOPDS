public class User {
    String userID;
    String password;

    User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String toCSVString() {
        String userType = "user";
        if (this instanceof Student) {
            userType = "student";
        } else if (this instanceof Lecturer) {
            userType = "lecturer";
        }
        return userID + "," + password + "," + userType;
    }
}
