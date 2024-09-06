public class Account {
    // The initial balance should be more than 500
    private double balance = 500.00;

    public void deposit(double amount) {
        balance += amount;
    }

    // It's not possible to withdraw more money than what you have
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds");
        }
    }

    public double getBalance() {
        return balance;
    }
}

