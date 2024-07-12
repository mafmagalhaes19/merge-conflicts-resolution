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

    public void showProduct() {
        System.out.println("Product Details - Name: " + productName + ", Price: $" + price);
    }

    public String fetchProductInfo() {
        return "Product [Name: " + productName + ", Price: $" + price + "]";
    }
}
