public class Inventory1 {
    private String itemName;
    private int quantity;
    private double pricePerUnit;

    public Inventory1(String itemName, int quantity, double pricePerUnit) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    // Added in branch A
    public double calculateTotalValue() {
        return quantity * pricePerUnit;
    }

    // Added in branch A
    public String getItemSummary() {
        return "Item: " + itemName + ", Quantity: " + quantity + ", Price per unit: $" + pricePerUnit;
    }
}

