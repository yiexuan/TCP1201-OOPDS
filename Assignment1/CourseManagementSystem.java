import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CourseManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Admin admin = new Admin("admin", "adminpass");
        List<Student> students = new ArrayList<>();
        List<Lecturer> lecturers = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
    
        TestData.createTestData(admin, students, lecturers, courses);
        LoadCourse.loadCourse(courses);

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
                    break;
                }
                else if (exitChoice.equals("no")) {
                    System.out.println();
                    System.out.println("Redirecting to login page.");
                    System.out.println();
                    break;
                }
                else{
                    System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
                }
            }
        }
    }

    public static void handleAdminActions(Admin admin, List<Student> students, List<Lecturer> lecturers,
            List<Course> courses) {
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
                        for (Student student : students) {
                            System.out.println(student.userID);
                        }
                        System.out.println();

                        admin.createStudent(students);
                        System.out.println("Updated list of students:");
                        for (Student student : students) {
                            System.out.println(student.userID);
                        }
                        System.out.println();
                        break;
                    case 2:
                        System.out.println("Current list of Lecturers:");
                        for (Lecturer lecturer : lecturers) {
                            System.out.println(lecturer.userID);
                        }
                        System.out.println();
                        admin.createLecturer(lecturers);
                        System.out.println("Updated list of Lecturers:");
                        for (Lecturer lecturer : lecturers) {
                            System.out.println(lecturer.userID);
                        }
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Current list of course:");
                        for (Course course : courses) {
                            System.out.println(course.courseCode);
                        }
                        System.out.println();
                        admin.createCourse(courses);
                        System.out.println("Updated list of course:");
                        for (Course course : courses) {
                            System.out.println(course.courseCode);
                        }
                        System.out.println();
                        break;
                    case 4:
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
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid choice. Error: Input must be a number");
                scanner.nextLine();
            }
        }
    }

    public static void handleStudentActions(Student student, List<Course> courses) {
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
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid choice. Error: Input must be a number");
                scanner.nextLine();
            }
        }
    }

    public static void handleLecturerActions(Lecturer lecturer, List<Course> courses) {
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
                }
            }catch (InputMismatchException e){
                System.out.println("Invalid choice. Error: Input must be a number");
                scanner.nextLine();
            }
        }
    }

    public static User findUser(String userID, String password, List<Student> students, List<Lecturer> lecturers) {
        for (Student student : students) {
            if (student.userID.equals(userID) && student.password.equals(password)) {
                return student;
            }
        }

        for (Lecturer lecturer : lecturers) {
            if (lecturer.userID.equals(userID) && lecturer.password.equals(password)) {
                return lecturer;
            }
        }
        return null;
    }
}