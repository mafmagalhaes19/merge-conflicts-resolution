public class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // The price should be a double
    public double getPrice() {
        return price;
    }
}

