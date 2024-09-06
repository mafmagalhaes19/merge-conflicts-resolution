public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    // This method should print a message to inform the user of the action
    public void deposit(double amount) {
        balance += amount;
    }

    // This method should print a message to inform the user of the action
    public void withdraw(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}

