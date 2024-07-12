public class Product1 {
    private String productName;
    private double price;

    public Product1(String productName, double price) {
        this.productName = productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public void displayProduct() {
        System.out.println("Product: " + productName + ", Price: $" + price);
    }

    // Print product price in euros
    public String getProductDetails() {
        return "Product Name: " + productName + ", Price: $" + price;
    }
}
