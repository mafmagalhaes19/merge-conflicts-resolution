public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful");
    }

    public void withdraw(double amount) {
        balance -= amount;
        System.out.println("Withdrawal successful");
    }

    public double getBalance() {
        return balance;
    }
}
