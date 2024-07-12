import java.util.ArrayList;
import java.util.List;

public class Student1 {
    private int id;
    private String name;
    private List<Integer> grades;

    public Student1(int id, String name) {
        this.id = id;
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public double getAverageGrade() {
        if (grades.isEmpty()) return 0.0;
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return sum / (double) grades.size();
    }

    public static void main(String[] args) {
        Student1 student = new Student1(1, "Alice");
        student.addGrade(90);
        student.addGrade(85);
        System.out.println("Student Name: " + student.getName());
        System.out.println("Average Grade: " + student.getAverageGrade());
    }
}
