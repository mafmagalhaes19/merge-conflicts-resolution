public class Account2 {
    private double balance;

    public Account2(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public static void main(String[] args) {
        Account2 account = new Account2(1000);
        account.deposit(200);
        System.out.println("Branch B: Account balance is " + account.getBalance());
    }
}
