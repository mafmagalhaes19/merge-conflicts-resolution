from transformers import GPT2Tokenizer

tokenizer = GPT2Tokenizer.from_pretrained("gpt2")

prompt_content = """
Prompt: role: user
content: I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java.

role: user
content: File 1:
public class BankAccount1 {
    private int accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount1(int accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    public static void main(String[] args) {
        BankAccount1 account = new BankAccount1(12345, "Alice", 1000);
        account.deposit(500);
        account.withdraw(300);
        System.out.println("Account Holder: " + account.getAccountHolderName());
        System.out.println("Balance: " + account.getBalance());
    }
}


role: user
content: File 2:
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


role: assistant
content: Resolution:
public class BankAccount0 {
    private int accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount0(int accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    public static void main(String[] args) {
        BankAccount0 account = new BankAccount0(12345, "Alice", 1000);
        account.deposit(500);
        account.withdraw(300);
        System.out.println("Account Holder: " + account.getAccountHolderName());
        System.out.println("Balance: " + account.getBalance());
    }
}


I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java.
role: user
content: File 1:
public class Account {
    private double balance = 1000.00;

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}



role: user
content: File 2:
public class Account {
    private double balance = 500.00;

    public void deposit(double amount) {
        balance += amount;
    }

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



role: assistant
content: Resolution:

public class Account {
    private double balance = 1000.00;

    public void deposit(double amount) {
        balance += amount;
    }

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
"""

tokens = tokenizer.encode(prompt_content)
print(len(tokens))
