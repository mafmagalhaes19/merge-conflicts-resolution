public class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    // The price should be a double
    public int getPrice() {
        return price;
    }
}

