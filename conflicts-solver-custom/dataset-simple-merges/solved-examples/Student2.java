import java.util.HashSet;
import java.util.Set;

public class Student2 {
    private int studentId;
    private String studentName;
    private Set<Integer> marks;

    public Student2(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.marks = new HashSet<>();
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Set<Integer> getMarks() {
        return marks;
    }

    public void addMark(int mark) {
        marks.add(mark);
    }

    public double calculateAverage() {
        if (marks.isEmpty()) return 0.0;
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return sum / (double) marks.size();
    }

    public static void main(String[] args) {
        Student2 student = new Student2(1, "Bob");
        student.addMark(90);
        student.addMark(85);
        System.out.println("Student Name: " + student.getStudentName());
        System.out.println("Average Mark: " + student.calculateAverage());
    }
}
