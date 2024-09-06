public class ComplexPerson {
    private String name;
    private int age;

    public ComplexPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void haveBirthday() {
        age += 1;
        System.out.println("Happy birthday " + name + "!");
    }
}

