public class Car {
    private double fuelLevel = 50.0;

    public void drive() {
        fuelLevel -= 10;
    }

    public void refuel() {
        fuelLevel = 100.0;
    }
}

