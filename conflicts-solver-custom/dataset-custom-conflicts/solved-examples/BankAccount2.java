public class BankAccount2 {
    private int accNumber;
    private String holderName;
    private double currentBalance;

    public BankAccount2(int accNumber, String holderName, double currentBalance) {
        this.accNumber = accNumber;
        this.holderName = holderName;
        this.currentBalance = currentBalance;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            currentBalance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && currentBalance >= amount) {
            currentBalance -= amount;
        }
    }

    public static void main(String[] args) {
        BankAccount2 account = new BankAccount2(12345, "Bob", 1000);
        account.deposit(500);
        account.withdraw(300);
        System.out.println("Account Holder: " + account.getHolderName());
        System.out.println("Balance: " + account.getCurrentBalance());
    }
}
