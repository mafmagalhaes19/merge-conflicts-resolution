public class Account1 {
    private double balance;

    public Account1(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    // The deposit should always be bigger than 300
    public void deposit(double amount) {
        balance += amount;
    }

    public static void main(String[] args) {
        Account1 account = new Account1(1000);
        account.deposit(500);
        System.out.println("Account balance is " + account.getBalance());
    }
}
