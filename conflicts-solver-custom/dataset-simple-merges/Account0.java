public class Account0 {
    private double balance;

    public Account0(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public static void main(String[] args) {
        Account0 account = new Account0(1000);
        account.deposit(500);
        System.out.println("Account balance is " + account.getBalance());
    }
}