package bankapp;

import java.time.LocalDateTime;

public class Transaction {
    private String type; // deposit or withdraw
    private double amount;
    private String timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String toString() {
        return type + ": $" + amount + " at " + timestamp;
    }
}
