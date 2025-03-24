import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Account {
    private double balance;
    private String userName;
    private String password;
    private List<String> transactionHistory;

    // For when we implement interest rate for savings, credit card, track what type of account
    private double interestSRate;   // Savings interest rate
    private int interestCCRate;     // Credit card interest rate
    private char accountType;       // 'C' for Checking, 'S' for Savings

    // Constructor
    public Account() {
        this.userName = "";
        this.password = "";
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
        this.interestSRate = 0.0;
        this.interestCCRate = 0;
        this.accountType = 'C';
    }

    // Setters
    public void setUserName(String u) {
        this.userName = u;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public void setBalance(double b) {
        this.balance = b;
    }

    // Getters
    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public double getBalance() {
        return this.balance;
    }

    public List<String> getTransactionHistory() {
        return this.transactionHistory;
    }

    // Deposit method
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
        recordTransaction("Deposited: $" + amount);
    }

    // Withdraw method
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (balance < amount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        this.balance -= amount;
        recordTransaction("Withdrew: $" + amount);
    }

    // keeps track of the transactions
    public void recordTransaction(String transactionDetail) {
        transactionHistory.add(transactionDetail);
    }

    // makes sure the login password is correct. 
    public boolean validatePassword(String inputPassword) {
        return inputPassword.equals(this.password);
    }
}
