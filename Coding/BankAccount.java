package Coding;

public class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount < balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        BankAccount acc = new BankAccount("12343568", 0);

        System.out.println("Initial balance: " + acc.getBalance());

        acc.deposit(100);
        System.out.println("After deposit: " + acc.getBalance());

        acc.withdraw(30);
        System.out.println("After withdraw: " + acc.getBalance());

        boolean result = acc.withdraw(1000);
        System.out.println("Withdraw success? " + result);
    }
}
