public class Person1 {
    private String firstName;
    private String lastName;

    public Person1(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static void main(String[] args) {
        Person1 person = new Person1("John", "Doe");
        System.out.println(person.getFirstName() + " " + person.getLastName());
    }
}
