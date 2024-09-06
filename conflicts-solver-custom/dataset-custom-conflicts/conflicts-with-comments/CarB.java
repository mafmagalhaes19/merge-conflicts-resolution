public class Car {
    private double fuelLevel = 50.0;

    public void drive() {
        fuelLevel -= 10;
        System.out.println("Driving the car");
    }

    public void refuel() {
        fuelLevel = 100.0;
        System.out.println("Car refueled");
    }
}

