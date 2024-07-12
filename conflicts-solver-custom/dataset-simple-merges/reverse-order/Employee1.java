import java.util.HashSet;
import java.util.Set;

public class Employee1 {
    private int id;
    private String name;
    private double salary;
    private Set<String> projects;

    public Employee1(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.projects = new HashSet<>();
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

    public Set<String> getProjects() {
        return projects;
    }

    public void increaseSalary(double percentage) {
        this.salary += this.salary * (percentage / 100);
    }

    public static void main(String[] args) {
        Employee1 employee = new Employee1(2, "Bob", 60000);
        employee.addProject("Project A");
        employee.increaseSalary(10);
        System.out.println("Employee name is " + employee.getName());
        System.out.println("Employee salary is " + employee.getSalary());
        System.out.println("Employee projects are " + employee.getProjects());
    }
}

