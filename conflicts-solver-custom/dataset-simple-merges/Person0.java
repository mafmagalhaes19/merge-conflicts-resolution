public class Person0 {
    private String firstName;
    private String lastName;

    public Person0(String firstName, String lastName) {
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
        Person0 person = new Person0("Jane", "Smith");
        System.out.println(person.getFirstName() + " " + person.getLastName());
    }
}
