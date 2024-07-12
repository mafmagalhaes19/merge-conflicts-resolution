public class Inventory2 {
    private String itemName;
    private int quantity;
    private double pricePerUnit;

    public Inventory2(String itemName, int quantity, double pricePerUnit) {
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
    
    // The total value should include taxes
    public double computeTotalValue() {
        return quantity * pricePerUnit * 1.1; // Includes a 10% tax
    }

    public String fetchItemSummary() {
        return "Item: " + itemName + " | Quantity: " + quantity + " | Unit Price: $" + pricePerUnit;
    }
}
