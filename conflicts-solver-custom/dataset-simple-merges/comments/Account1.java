public class Account1 {
    private double balance;

    public Account1(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public static void main(String[] args) {
        Account1 account = new Account1(1000);
        // The deposit should always be higher than 300
        account.deposit(500);
        System.out.println("Branch A: Account balance is " + account.getBalance());
    }
}
