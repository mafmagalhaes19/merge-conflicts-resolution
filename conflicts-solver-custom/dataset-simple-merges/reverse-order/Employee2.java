import java.util.ArrayList;
import java.util.List;

public class Employee2 {
    private int id;
    private String name;
    private double salary;
    private List<String> projects;

    public Employee2(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.projects = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void addProject(String project) {
        projects.add(project);
    }

    public void removeProject(String project) {
        projects.remove(project);
    }

    public List<String> getProjects() {
        return projects;
    }

    public void increaseSalary(double amount) {
        this.salary += amount;
    }

    public static void main(String[] args) {
        Employee2 employee = new Employee2(1, "Alice", 50000);
        employee.addProject("Project A");
        employee.increaseSalary(5000);
        System.out.println("Employee name is " + employee.getName());
        System.out.println("Employee salary is " + employee.getSalary());
        System.out.println("Employee projects are " + employee.getProjects());
    }
}

