public class Car {
    private double fuelLevel = 50.0;

    // Print a message for the user
    public void drive() {
        fuelLevel -= 10;
        System.out.println("Driving the car");
    }

    // Print a message for the user
    public void refuel() {
        fuelLevel = 100.0;
        System.out.println("Car refueled");
    }
}

