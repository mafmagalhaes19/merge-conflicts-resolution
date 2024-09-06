public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    // This method should print a message to inform the user of the action
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful");
    }

    // This method should print a message to inform the user of the action
    public void withdraw(double amount) {
        balance -= amount;
        System.out.println("Withdrawal successful");
    }

    public double getBalance() {
        return balance;
    }
}

