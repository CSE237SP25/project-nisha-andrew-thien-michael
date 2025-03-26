import java.util.UUID;

public class BankAccount {
    private String accountType; // checking or savings 
    private String accountNumber; // random generated account number
    private double balance; // current balance
    private double monthlyLimit; // monthly spending limit 
    private double currentSpent; // current spent this month

    // constructor to validate input
    public BankAccount(String accountType) {
        if (!accountType.equalsIgnoreCase("Checking") && !accountType.equalsIgnoreCase("Savings")) {
            throw new IllegalArgumentException("Account type must be 'Checking' or 'Savings'.");
        }

        this.accountType = accountType;
        this.accountNumber = generateAccountNumber(accountType);
        this.balance = 0.0;
        this.monthlyLimit = Double.MAX_VALUE;
        this.currentSpent = 0.0;
    }

    // private method to create unique account number 
    private String generateAccountNumber(String type) {
        String prefix = type.equalsIgnoreCase("Checking") ? "CHK" : "SVG";
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public void deposit(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Deposit must be positive.");
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Withdrawal must be positive.");
        if (amount > balance)
            throw new IllegalArgumentException("Insufficient funds.");
        if ((currentSpent + amount) > monthlyLimit)
            throw new IllegalArgumentException("Exceeds monthly spending limit.");

        this.balance -= amount;
        this.currentSpent += amount;
    }

    public void setMonthlyLimit(double limit) {
        if (limit < 0)
            throw new IllegalArgumentException("Limit must be non-negative.");
        this.monthlyLimit = limit;
    }

    public void resetMonthlySpent() {
        this.currentSpent = 0;
    }

    public double getBalance() {
        return this.balance;
    }

    public double getMonthlyLimit() {
        return this.monthlyLimit;
    }

    public double getCurrentSpent() {
        return this.currentSpent;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }
}
