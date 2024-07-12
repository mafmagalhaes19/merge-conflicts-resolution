public class Product2 {
    private String productName;
    private double price;

    public Product2(String productName, double price) {
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

    public String getProductDetails() {
        return "Product Name: " + productName + ", Price: $" + price;
    }
}
