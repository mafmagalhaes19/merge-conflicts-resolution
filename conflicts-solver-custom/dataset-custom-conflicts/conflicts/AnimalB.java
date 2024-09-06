public class Animal {
    public void sound() {
        System.out.println("Some generic animal sound");
    }
}

public class Dog extends Animal {
    @Override
    public void sound() {
        System.out.println("Woof");
    }
}

