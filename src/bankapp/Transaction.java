package bankapp;

import java.time.LocalDateTime;
import java.util.Locale;

public class Transaction {
    private String type; // deposit or withdraw
    private double amount;
    private String timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now().toString();
    }
    
    public double getAmount() {
    	return this.amount;
    }
    
    public String getType() {
    	return this.type;
    }

    @Override
    public String toString() {
    	return String.format(Locale.US, "%s: $%.2f at %s",
                this.type,
                this.amount, 
                this.timestamp);
    }
   
}
