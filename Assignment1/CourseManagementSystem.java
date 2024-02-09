import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CourseManagementSystem {
    static String userFilename = "user.csv";
    static String courseFilename = "course.csv";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Admin admin = new Admin("admin", "adminpass");
        Map<String, Student> students = readUsersFromFile("student");
        Map<String, Lecturer> lecturers = readUsersFromFile("lecturer");
        Set<Course> courses = readCourseFromFile(lecturers);
        // readCourseFromFile(lecturers);
    
        // TestData.createTestData(admin, students, lecturers);
        // LoadCourse.loadCourse(courses);

        System.out.println("Welcome to Course Management System");
        while (true) {
            //System.out.println("Welcome to Course Management System");
            System.out.println("Please provide your ID and password.");
            System.out.print("User ID: ");
            String userID = scanner.next();
            System.out.print("Password: ");
            String password = scanner.next();
            System.out.println();

            if (userID.equals(admin.userID) && password.equals(admin.password)) {
                handleAdminActions(admin, students, lecturers, courses);
            } else {
                User currentUser = findUser(userID, password, students, lecturers);
                if (currentUser != null) {
                    if (currentUser instanceof Student) {
                        handleStudentActions((Student) currentUser, courses);
                    } else if (currentUser instanceof Lecturer) {
                        handleLecturerActions((Lecturer) currentUser, courses);
                    }
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                    System.out.println();
                    continue;
                }
            }

            while(true) {
                System.out.print(
                        "Do you want to exit the program? (yes/no) If no, you will be returning to the login page : ");
                String exitChoice = scanner.next().toLowerCase();
                if (exitChoice.equals("yes")) {
                    System.out.println("Exiting. Thank you for using the Course Management System.");
                    System.out.println();
                    return;
                }
                else if (exitChoice.equals("no")) {
                    System.out.println();
                    System.out.println("Redirecting to login page.");
                    System.out.println();
                    break;
                }
                else{
                    System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
                    System.out.println();
                }
            }
        }
    }

    public static void handleAdminActions(Admin admin, Map<String, Student> students, Map<String, Lecturer> lecturers,
            Set<Course> courses) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try{
                System.out.println("Admin Dashboard");
                System.out.println("1. Create Student");
                System.out.println("2. Create Lecturer");
                System.out.println("3. Create Course");
                System.out.println("4. Assign Course to Lecturer");
                System.out.println("5. View students and lecturers for course");
                System.out.println("6. Logout");

                System.out.print("Please enter your choice: ");
                int choice = scanner.nextInt();
                System.out.println();

                switch (choice) {
                    case 1:
                        System.out.println("Current list of students:");
                        for (String userID : students.keySet()) {
                            System.out.println(userID);
                        }
                        System.out.println();

                        admin.createStudent(students, lecturers);
                        System.out.println();
                        break;
                    case 2:
                        System.out.println("Current list of Lecturers:");
                        for (String userID: lecturers.keySet()) {
                            System.out.println(userID);
                        }
                        System.out.println();
                        admin.createLecturer(lecturers, students);
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Current list of course:");
                        for (Course course : courses) {
                            System.out.println(course.courseCode);
                        }
                        System.out.println();
                        admin.createCourse(courses);
                        System.out.println();
                        break;
                    case 4:
                    // for (Lecturer lecturer : lecturers) {
                    //     System.out.println(lecturer.userID);}
                        admin.assignCourseToLecturer(courses, lecturers);
                        break;
                    case 5:
                        admin.viewStudentsAndLecturersForCourse(courses);
                        break;
                    case 6:
                        System.out.println("Logout. Thank you for using the Course Management System.");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        System.out.println();
                        break;
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid choice. Error: Input must be a number\n");
                scanner.nextLine();
            }
        }
    }

    public static void handleStudentActions(Student student, Set<Course> courses) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try{
                System.out.println("Student Dashboard");
                System.out.println("1. Register Course");
                System.out.println("2. View Registered Course");
                System.out.println("3. Logout");

                System.out.print("Please enter your choice: ");
                int choice = scanner.nextInt();
                System.out.println();

                switch (choice) {
                    case 1:
                        student.registerCourse(courses);
                        break;
                    case 2:
                        student.viewRegisteredCourses();
                        break;
                    case 3:
                        System.out.println("Logout. Thank you for using the Course Management System.");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        System.out.println();
                        break;
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid choice. Error: Input must be a number\n");
                scanner.nextLine();
            }
        }
    }

    public static void handleLecturerActions(Lecturer lecturer, Set<Course> courses) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try{
                System.out.println("Lecturer Dashboard");
                System.out.println("1. View Student in Courses");
                System.out.println("2. Logout");

                System.out.print("Please enter your choice: ");
                int choice = scanner.nextInt();
                System.out.println();

                switch (choice) {
                    case 1:
                        lecturer.viewStudentsInCourses(courses);
                        break;
                    case 2:
                        System.out.println("Logout. Thank you for using the Course Management System.");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        System.out.println();
                        break;
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid choice. Error: Input must be a number\n");
                scanner.nextLine();
            }
        }
    }

    public static User findUser(String userID, String password, Map<String, Student> students, Map<String, Lecturer> lecturers) {
        Student student = students.get(userID);     //check if the ID exist in the map
            if (student != null && student.password.equals(password)) {
                return student;
            }

        Lecturer lecturer = lecturers.get(userID); 
            if (lecturer != null && lecturer.password.equals(password)) {
                return lecturer;
            }
        return null;
    }

    private static <E extends User> Map<String, E> readUsersFromFile(String userType) {
        Map<String, E> users = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(userFilename));
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
    // private static Set<Course> readCourseFromFile(Map<String, Lecturer> lecturers) {
    //     Set<Course> courses = new LinkedHashSet<>();
    //     try {
    //       List<String> lines = Files.readAllLines(Paths.get(courseFilename));
    //       for (int i = 0; i < lines.size(); i++) {
    //         String[] items = lines.get(i).split(",");
    
    //         int credits = Integer.parseInt(items[0]);
    //         String courseCode = items[1];
     
    //         Set<String> prerequisites = new LinkedHashSet<>();
    //         for (int n = 2; n < items.length; n++) {
    //             String[] prerequisitesArray = items[n].replaceAll("^\"|\"$", "").split(",");
    //         for (String prerequisite : prerequisitesArray) {
    //             prerequisites.add(prerequisite.trim());
    //         }
    //         }
    //         System.out.println(prerequisites);
    //         // Check if lecturer is provided
    //         Lecturer assignedLecturer = null;
    //         if (items.length > 3 && !items[3].isEmpty()) {
    //             assignedLecturer = lecturers.get(items[3]);
    //         }
    //         System.out.println(assignedLecturer);
    //         //Lecturer assignedLecturer = lecturers.get(items[3]);
    //         List<Student> studentsEnrolled = new ArrayList<>();
    //         Course course = new Course(credits, courseCode, prerequisites);
    //         courses.add(course);
    //       }
    //     } catch (IOException ex) {
    //       System.out.println(ex.getMessage());
    //     }
    //     return courses;
    // }
    private static Set<Course> readCourseFromFile(Map<String, Lecturer> lecturers) {
    Set<Course> courses = new LinkedHashSet<>();
    try {
        List<String> lines = Files.readAllLines(Paths.get(courseFilename));
        for (String line : lines) {
            //  String[] items;
            //  items = line.split(",");
            String[] items = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            int credits = Integer.parseInt(items[0]);
            String courseCode = items[1];

            String tempPrerequisites = items[2];
                System.out.println(tempPrerequisites);
            // Check if lecturer is provided
            Lecturer assignedLecturer = null;
            if (items.length > 3 && !items[3].isEmpty()) {
                assignedLecturer = lecturers.get(items[3]);
            }
            System.out.println(assignedLecturer);

            List<Student> studentsEnrolled = new ArrayList<>();
            Course course = new Course(credits, courseCode, tempPrerequisites);
            courses.add(course);
        }
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
    }
    return courses;
}
}