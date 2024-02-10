import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Student extends User {
    // read student.csv file
    Set<String> registeredCourses = getRegisteredCourses();

    public Student(String userID, String password) {
        super(userID, password);
        this.registeredCourses = getRegisteredCourses();
    }

    void registerCourse(Set<Course> courses) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("List of Course Code:");
        for (Course course : courses) {
            System.out.println(course.courseCode);
        }
        while (true) {
            System.out.print("Please provide course that you want to register: ");
            String selectedCourseCode = scanner.next().toUpperCase();

            Course selectedCourse = findCourse(courses, selectedCourseCode);

            if (selectedCourse != null) {
                boolean studentAlreadyRegistered = registeredCourses.contains(selectedCourseCode);
                // check credit hours
                // if (!checkCreditHours(selectedCourseCode, this.userID))
                //     break;
                // check prerequisites
                if (!checkPreRequisites(selectedCourseCode, this.userID))
                    break;

                if (!studentAlreadyRegistered) {
                    registeredCourses.add(selectedCourseCode);
                    studentAlreadyRegistered = selectedCourse.studentsEnrolled.contains(this);
                    // studentAlreadyRegistered = selectedCourse.assignedLecturer.studentsInCourse.contains(this);
                    // if (!studentAlreadyRegistered)
                    //     selectedCourse.assignedLecturer.studentsInCourse.add(this);
                    selectedCourse.studentsEnrolled.add(this);
                    addCourseToStudentFile(this.userID, selectedCourse);
                    addCreditHours(selectedCourseCode);
                    System.out.println("Course registered successfully.");
                    System.out.println();
                    break;
                } else {
                    System.out.println("Student already registered for the course " + selectedCourseCode + ".");
                    System.out.println();
                    break;
                }

            } else {
                System.out.println("Invalid course selection.Please try again.");
                System.out.println();
                continue;
            }
        }

    }

    // get registered courses from student.csv
    public Set<String> getRegisteredCourses() {
        registeredCourses = new LinkedHashSet<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[0].equals(this.userID)) {
                    for (int i = 2; i < items.length; i++) {
                        registeredCourses.add(items[i]);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return registeredCourses;
    }

    public void viewRegisteredCourses() {
        System.out.println("Registered Course:");
        for (String course : registeredCourses) {
            System.out.println(course);
        }
        System.out.println();
    }

    public Course findCourse(Set<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    public static void readStudentFile() {
        try {

            List<String> lines = Files.readAllLines(Paths.get("user.csv"));
            List<String> firstColumn = new ArrayList<>();
            for (String line : lines) {
                String[] items = line.split(",");
                firstColumn.add(items[0]);
            }
            Files.write(Paths.get("student.csv"), firstColumn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean checkCreditHours(String selectedCourse, String studentId) {
        int creditHours = 0;
        // get credit hours from course.csv
        try {
            List<String> lines = Files.readAllLines(Paths.get("course.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[1].equals(selectedCourse)) {
                    creditHours = Integer.parseInt(items[0]);
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        // get credit hour column from student.csv
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            for (String studentLine : studentLines) {
                String[] studentItems = studentLine.split(",");
                if (studentItems[0].equals(studentId)) {
                    int currentCreditHours = Integer.parseInt(studentItems[1]);
                    if (currentCreditHours + creditHours > 12) {
                        System.out.println("\nCredit hours exceeded the limit of 12.\n");
                        return false;
                    }
                    else if (currentCreditHours + creditHours < 3) {
                        System.out.println("\nCredit hours should be at least 3.\n");
                        return false;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    private static void addCourseToStudentFile(String studentId, Course course) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            List<String> updatedLines = new ArrayList<>();

            // Iterate through each line in the file
            for (String line : lines) {
                String[] parts = line.split(","); // Split the line by comma
                
                // Check if the first part (student ID) matches the given studentId
                if (parts.length > 0 && parts[0].equals(studentId)) {
                    // Append the course code to the current line
                    StringBuilder updatedLine = new StringBuilder(line);
                    updatedLine.append(course.courseCode).append(",");
                    updatedLines.add(updatedLine.toString());
                } else {
                    updatedLines.add(line); // Keep the line unchanged
                }
            }

            // Write the updated content back to the file
            Files.write(Paths.get("student.csv"), updatedLines);
            System.out.println("Course added successfully for student: " + studentId);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

        // Path path = Paths.get("student.csv");
        // try {
        //     List<String> lines = Files.readAllLines(path);
        //     List<String> modifiedLines = new ArrayList<>();
        //     for (String line : lines) {
        //         String[] items = line.split(",");
        //         if (items[0].equals(studentId)) {
        //             line += "," + String.join(",", newCourses);
        //         }
        //         modifiedLines.add(line);
        //     }
        //     Files.write(path, modifiedLines);
        // } catch (IOException ex) {
        //     System.out.println(ex.getMessage());
        // }


    // add credit hours to student
    public void addCreditHours(String selectedCourse) {
        int creditHours = 0;
        // get credit hours from course.csv
        try {
            List<String> lines = Files.readAllLines(Paths.get("course.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[1].equals(selectedCourse)) {
                    creditHours = Integer.parseInt(items[0]);
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        // get credit hour column from student.csv
        try {
            List<String> studentLines = Files.readAllLines(Paths.get("student.csv"));
            List<String> updatedStudentLines = new ArrayList<>();
            for (String studentLine : studentLines) {
                String[] studentItems = studentLine.split(",");
                if (studentItems[0].equals(this.userID)) {
                    int currentCreditHours = Integer.parseInt(studentItems[1]);
                    int newCreditHours = currentCreditHours + creditHours;
                    studentItems[1] = String.valueOf(newCreditHours); // Update credit hours
                    studentLine = studentItems[0] + "," + studentItems[1] + "," + String.join(",", registeredCourses);
                }
                updatedStudentLines.add(studentLine);
            }

            // Write the updated student data back to student.csv
            Files.write(Paths.get("student.csv"), updatedStudentLines);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean checkPreRequisites(String courseCode, String studentId) {
        if (courseCode.equals("CS214"))
            return cs214Checker(studentId);
        else if (courseCode.equals("CS224"))
            return cs224Checker(studentId);
        else if (courseCode.equals("CS316"))
            return cs316Checker(studentId);
        else
            return true;
    }

    public static boolean cs214Checker(String studentId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[0].equals(studentId)) {
                    for (int i = 2; i < items.length; i++) {
                        if (items[i].equals("CS113"))
                            return true;
                    }
                }
            }
            System.out.println("\nYou need to register CS113 before taking CS214.\n");
            return false;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static boolean cs224Checker(String studentId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[0].equals(studentId)) {
                    for (int i = 2; i < items.length; i++) {
                        if (items[i].equals("CS123"))
                            return true;
                    }
                }
            }
            System.out.println("\nYou need to register CS123 before taking CS224.\n");
            return false;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static boolean cs316Checker(String studentId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[0].equals(studentId)) {

                    for (int i = 2; i < items.length; i++) {
                        if (items[i].equals("CS133")) {

                            for (int j = 2; j < items.length; j++) {
                                if (items[j].equals("CS214")) {

                                    int temp = Integer.parseInt(items[1]);
                                    if (temp >= 15)
                                        return true;
                                    else
                                        System.out.println("\nYou need at least 15 credit hours before taking CS316.\n");
                                        return false;                        
                                }
                            }
                            System.out.println("\nYou need to register CS214 before taking CS316.\n");
                            return false;
                        }
                    }
                    System.out.println("\nYou need to register CS133 before taking CS316.\n");
                    return false;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

}

