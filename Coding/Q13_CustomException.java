package Coding;

public class Q13_CustomException {
    static class  InsufficientBalanceException extends Exception {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    static class Wallet {
        private double balance;

        public Wallet(double balance) {
            this.balance = balance;
        }

        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit must be positive");
            }
            balance += amount;
        }

        public void withdraw(double amount) throws InsufficientBalanceException{
            if (balance < amount) {
                throw new InsufficientBalanceException("Insufficient Balance");
            }
            balance -= amount;
        }

        public double getBalance() {
            return balance;
        }
    }

    public static void main(String[] args) {
        Wallet wallet = new Wallet(100);

        wallet.deposit(50);

        try {
            wallet.withdraw(200);
        } catch (InsufficientBalanceException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        System.out.println("Final balance: " + wallet.getBalance());
    }
}
