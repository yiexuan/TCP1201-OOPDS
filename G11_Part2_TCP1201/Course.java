import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The Course class represents a course in the 1-year certificate program.
 */
public class Course {
    int credits;
    String courseCode;
    String prerequisites;
    Lecturer assignedLecturer = getAssignedLecturer();
    List<Student> studentsEnrolled = getStudentsEnrolled();

    /**
     * Constructs a new Course object with the specified courseCode.
     * @param courseCode course code of the course
     */
    public Course(String courseCode){
        this.courseCode = courseCode;
    }

     /**
     * Constructs a new Course object with the specified credits, courseCode and
     * prerequisites.
     * @param credits number of credits for the course
     * @param courseCode course code of the course
     * @param prerequisites prerequisites for the course
     */
    public Course(int credits, String courseCode, String prerequisites) {
        this.credits = credits;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
        this.assignedLecturer = getAssignedLecturer();
        this.studentsEnrolled = getStudentsEnrolled();
    }

    /**
     * Converts the Course object to a CSV string format.
     * The format of the CSV string is: credits,courseCode,prerequisites
     * 
     * @return the CSV representation of the Course object
     */
    public String toCSVString() {
        return credits + "," + courseCode + "," + prerequisites;
    }

    /**
     * Reads the lecturer.csv file and returns the assigned lecturer for the course.
     * @return the assigned lecturer for the course
     */
    public Lecturer getAssignedLecturer() {
        assignedLecturer = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get("lecturer.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                for (int i = 2; i < items.length; i++) {
                    if (items[i].equals(this.courseCode)) {
                        //String str = items[0];
                        assignedLecturer = new Lecturer(items[0], items[1], null);
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return assignedLecturer;
    }

    /**
     * Reads the student.csv file and returns a list of students enrolled in the specific course.
     * @return a list of students enrolled in the course
     */
    public List<Student> getStudentsEnrolled() {
        studentsEnrolled = new ArrayList<>();
        
        try {
            List<String> lines = Files.readAllLines(Paths.get("student.csv"));
            for (String line : lines) {
                String[] items = line.split(",");
                // if (items.length >= 4 && items[2].equals(this.courseCode)) { // Check if enough elements and courseCode matches
                //     this.studentsEnrolled.add(new Student(items[0], items[1], null));
                // }
                for (int i = 3; i < items.length; i++) {
                    if (items[i].equals(this.courseCode))
                        this.studentsEnrolled.add(new Student(items[0], items[1], null));
                }
            }
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return this.studentsEnrolled;
    }
    
    /**
     * Returns the course code.
     * @return the course code
     */
    public String getCourseCode(){
        return courseCode;
    }

    /**
     * Sets the assigned lecturer for the course.
     * 
     * @param assignedLecturer the assigned lecturer for the course
     */
    public void setAssignedLecturer(Lecturer assignedLecturer) {
        this.assignedLecturer = assignedLecturer;
    }

    /**
     * Returns a string representation of the courseCode of the Course object. 
     * @return the course code
     */
    public String toString() {
        return courseCode; 
    }
}