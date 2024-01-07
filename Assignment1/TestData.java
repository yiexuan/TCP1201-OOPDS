import java.util.List;


public class TestData {

    public static void createTestData(Admin admin, List<Student> students, List<Lecturer> lecturers) {

        Lecturer newLecturer = new Lecturer("lec001", "lecpass");
        lecturers.add(newLecturer);

        Student newStudent = new Student("stu001", "stupass");
        students.add(newStudent);
    }
}
