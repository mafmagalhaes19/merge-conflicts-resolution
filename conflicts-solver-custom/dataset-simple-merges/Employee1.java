import java.util.ArrayList;
import java.util.List;

public class Employee1 {
    private int id;
    private String name;
    private double salary;
    private List<String> projects;

    public Employee1(int id, String name, double salary) {
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
        Employee1 employee = new Employee1(1, "Alice", 50000);
        employee.addProject("Project A");
        employee.increaseSalary(5000);
        System.out.println("Branch A: Employee name is " + employee.getName());
        System.out.println("Branch A: Employee salary is " + employee.getSalary());
        System.out.println("Branch A: Employee projects are " + employee.getProjects());
    }
}

