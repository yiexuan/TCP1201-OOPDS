import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Admin extends User {
    public Admin(String userID, String password) {
        super(userID, password);
    }

    public void createStudent(Map<String, Student> students, Map<String, Lecturer> lecturers) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("New student ID: ");
            String newStudentID = scanner.next().toLowerCase();
            if (isUserIDTaken(newStudentID, students, lecturers) && isStudentExist(newStudentID)) {
                System.out.println("Student ID already exists. Please choose a different ID.");
                System.out.println();
                continue;
            } else {
                System.out.print("New student password: ");
                String newStudentPassword = scanner.next();
                Student newStudent = new Student(newStudentID, newStudentPassword);
                students.put(newStudentID, newStudent);
                saveUserToFile(students, lecturers);
                saveStudentToFile(newStudent);
                System.out.println("A new student has been created.");
                System.out.println();
                System.out.println("Updated list of students:");
                for (var entry : students.entrySet()) {
                    System.out.println(entry.getKey());
                }
                break;
            }
        }
    }

    public void createLecturer(Map<String, Lecturer> lecturers, Map<String, Student> students) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("New lecturer ID: ");
            String newLecturerID = scanner.next().toLowerCase();
            if (isUserIDTaken(newLecturerID, students, lecturers)) {
                System.out.println("Lecturer ID already exists. Please choose a different ID.");
                System.out.println();
                continue;
            } else {
                System.out.print("New lecturer password: ");
                String newLecturerPassword = scanner.next();
                Lecturer newLecturer = new Lecturer(newLecturerID, newLecturerPassword);
                lecturers.put(newLecturerID, newLecturer);
                saveUserToFile(students, lecturers);
                System.out.println("A new lecturer has been created.");
                System.out.println();
                System.out.println("Updated list of Lecturers:");
                for (var entry : lecturers.entrySet()) {
                    System.out.println(entry.getKey());
                }
                break;
            }
        }
    }

    public void createCourse(Set<Course> courses) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("New Course Code: ");
            String newCourseCode = scanner.next().toUpperCase();

            if (isCourseCodeTaken(newCourseCode, courses)) {
                System.out.println("Course Code already exists. Please choose a different code.");
                System.out.println();
                continue;
            } else {
                System.out.print("Course Credit: ");
                int newCourseCredit = scanner.nextInt();
                System.out.print("Prerequisite (comma-separated, enter 'Nil' for if no prerequisite): ");
                // String[] prereqs = scanner.next().split(",");
                // Set<String> prerequisites = new HashSet<>(Arrays.asList(prereqs));
                // Course newCourse = new Course(newCourseCredit, newCourseCode, prerequisites);
                // courses.add(newCourse);
                System.out.println("A new course has been created.");
                System.out.println("Updated list of course:");
                for (Course course : courses) {
                    System.out.println(course.courseCode);
                }
                System.out.println();
                break;
            }
        }
    }

    public void assignCourseToLecturer(Set<Course> courses, Map<String, Lecturer> lecturers) {
        if (courses.isEmpty() || lecturers.isEmpty()) {
            System.out.println("No courses or lecturers available.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        boolean validCourse = false;
        Course selectedCourse = null;

        while (!validCourse) {
            System.out.println("List of Courses:");
            for (Course course : courses) {
                System.out.println(course.courseCode);
            }

            System.out.print("Select a Course Code: ");
            String courseCode = scanner.next().toUpperCase();
            selectedCourse = findCourse(courses, courseCode);

            if (selectedCourse != null) {
                validCourse = true;
            } else {
                System.out.println("Course not found. Please try again.\n");
            }
        }

        boolean validLecturer = false;
        Lecturer selectedLecturer = null;

        while (!validLecturer) {
            System.out.println("List of Lecturers:");
            for (var userID : lecturers.keySet()) {
                System.out.println(userID);
            }

            System.out.print("Select a Lecturer ID: ");
            String lecturerID = scanner.next();
            selectedLecturer = findLecturer(lecturers, lecturerID);

            if (selectedLecturer != null) {
                validLecturer = true;
                if (selectedCourse.assignedLecturer == null) {
                    selectedCourse.assignedLecturer = selectedLecturer;

                    // courses.add(selectedCourse);
                    saveCourseToFile(courses);
                    System.out.println("A course has been assigned to Lecturer " + lecturerID + "\n");
                } else if (selectedCourse.assignedLecturer.equals(selectedLecturer))
                    System.out.println("Course already assigned to this lecturer\n");
            } else {
                System.out.println("Lecturer not found. Please try again.\n");
            }
        }
    }

    public void viewStudentsAndLecturersForCourse(Set<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        while (true) {
            System.out.println("List of Courses:");
            for (Course course : courses) {
                System.out.println(course.courseCode);
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Select a Course Code: ");
            String courseCode = scanner.next().toUpperCase();
            Course selectedCourse = findCourse(courses, courseCode);

            if (selectedCourse != null) {
                // if (!selectedCourse.assignedLecturers.isEmpty())
                // System.out.println("The course is taken by another lecturer. One course
                // should only have one lecturer.");
                // else{
                System.out.println("Course Code: " + selectedCourse.courseCode);
                System.out.println("List of Student:");
                for (Student s : selectedCourse.studentsEnrolled)
                    System.out.println(s.userID);
                System.out.println();
                System.out.println("Lecturer:");
                if (selectedCourse.assignedLecturer != null)
                    System.out.println(selectedCourse.assignedLecturer.userID);
                else
                    System.out.println("No lecturer assigned to this course.");
                System.out.println();
                break;
                // }
            } else {
                System.out.println("Course not found.Please select a valid course.\n");
                continue;
            }
        }
    }

    public Course findCourse(Set<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    public Lecturer findLecturer(Map<String, Lecturer> lecturers, String lecturerID) {
        return lecturers.get(lecturerID);
    }

    private boolean isUserIDTaken(String userID, Map<String, Student> students, Map<String, Lecturer> lecturers) {
        return students.containsKey(userID) || lecturers.containsKey(userID) || userID.equals("admin");
    }

    private boolean isCourseCodeTaken(String courseCode, Set<Course> courses) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return true;
            }
        }
        return false;
    }

    private static void saveUserToFile(Map<String, Student> students, Map<String, Lecturer> lecturers) {
        Map<String, User> users = new HashMap<>();
        users.putAll(students);
        users.putAll(lecturers);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ? extends User> entry : users.entrySet()) {
            sb.append(entry.getValue().toCSVString()).append("\n");
        }
        try {
            Files.write(Paths.get(CourseManagementSystem.userFilename), sb.toString().getBytes());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void saveStudentToFile(Student student) {
    StringBuilder sb = new StringBuilder();
    sb.append(student.userID).append(",\n");
    try {
        Files.write(Paths.get("student.csv"), sb.toString().getBytes(), StandardOpenOption.APPEND);
    } catch (IOException ex) {
        System.out.println(ex.getMessage());
    }
}

    private boolean isStudentExist(String studentID) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                if (items[0].equals(studentID)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private static void saveCourseToFile(Set<Course> courses) {
        StringBuilder sb = new StringBuilder();
        for (Course course : courses) {
            System.out.println(course.prerequisites);
        }
        for (Course course : courses) {
            sb.append(course.toCSVString()).append("\n");
        }
        try {
            Files.write(Paths.get(CourseManagementSystem.courseFilename), sb.toString().getBytes());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    // for (int i = 0; i < students.size(); i++)
    // sb.append(students.get(i).toCSVString() + "\n");
    // try {
    // Files.write(Paths.get(CourseManagementSystem.courseFilename),
    // sb.toString().getBytes());
    // } catch (IOException ex) {
    // System.out.println(ex.getMessage());
    // }
    // }
}
