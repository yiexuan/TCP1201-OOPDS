import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Admin extends User {
    public Admin(String userID, String password) {
        super(userID, password);
    }

    public void createStudent(List<Student> students) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("New student ID: ");
            String newStudentID = scanner.next().toLowerCase();
            if (isUserIDTaken(newStudentID, students)) {
                System.out.println("Student ID already exists. Please choose a different ID.");
                System.out.println();
                continue;
            } else {
                System.out.print("New student password: ");
                String newStudentPassword = scanner.next();
                Student newStudent = new Student(newStudentID, newStudentPassword);
                students.add(newStudent);
                System.out.println("A new student has been created.");
                System.out.println();
                System.out.println("Updated list of students:");
                for (Student student : students) {
                    System.out.println(student.userID);
                }
                break;
            }
        }
    }

    public void createLecturer(List<Lecturer> lecturers) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("New lecturer ID: ");
            String newLecturerID = scanner.next().toLowerCase();
            if (isUserIDTaken(newLecturerID, lecturers)) {
                System.out.println("Lecturer ID already exists. Please choose a different ID.");
                System.out.println();
                continue;
            } else {
                System.out.print("New lecturer password: ");
                String newLecturerPassword = scanner.next();
                Lecturer newLecturer = new Lecturer(newLecturerID, newLecturerPassword);
                lecturers.add(newLecturer);
                System.out.println("A new lecturer has been created.");
                System.out.println();
                System.out.println("Updated list of Lecturers:");
                for (Lecturer lecturer : lecturers) {
                    System.out.println(lecturer.userID);
                }
                break;
            }
        }

    }

    public void createCourse(List<Course> courses) {
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
                String[] prereqs = scanner.next().split(",");
                List<String> prerequisites = Arrays.asList(prereqs);
                Course newCourse = new Course(newCourseCode, newCourseCredit, prerequisites);
                courses.add(newCourse);
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

                public void assignCourseToLecturer(List<Course> courses, List<Lecturer> lecturers) {
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
                        for (Lecturer lecturer : lecturers) {
                            System.out.println(lecturer.userID);
                        }
                
                        System.out.print("Select a Lecturer ID: ");
                        String lecturerID = scanner.next();
                        selectedLecturer = findLecturer(lecturers, lecturerID);
                
                        if (selectedLecturer != null) {
                            validLecturer = true;
                            boolean lecturerAlreadyAssigned = selectedCourse.assignedLecturers.contains(selectedLecturer);
                            if (!lecturerAlreadyAssigned) {
                                selectedCourse.assignedLecturers.add(selectedLecturer);
                                System.out.println("A course has been assigned to Lecturer " + lecturerID + "\n");
                            }
                            else if (lecturerAlreadyAssigned)
                                System.out.println("Course already assigned to this lecturer\n");
                        }
                        else {
                            System.out.println("Lecturer not found. Please try again.\n");
                        }
                    }
                }
                

    public void viewStudentsAndLecturersForCourse(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        while(true){
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
            System.out.println("List of Lecturer:");
            for (Lecturer assignedLecturer : selectedCourse.assignedLecturers) {
                System.out.println(assignedLecturer.userID);
            }
            System.out.println();
            break;
            // }
        }
        else {
            System.out.println("Course not found.Please select a valid course.\n");
            continue;
        }
    }
    }

    public Course findCourse(List<Course> courses, String courseCode) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    public Lecturer findLecturer(List<Lecturer> lecturers, String lecturerID) {
        for (Lecturer lecturer : lecturers) {
            if (lecturer.userID.equals(lecturerID)) {
                return lecturer;
            }
        }
        return null;
    }

    private boolean isUserIDTaken(String userID, List<? extends User> users) {
        for (User user : users) {
            if (user.userID.equals(userID)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCourseCodeTaken(String courseCode, List<Course> courses) {
        for (Course course : courses) {
            if (course.courseCode.equals(courseCode)) {
                return true;
            }
        }
        return false;
    }
}
