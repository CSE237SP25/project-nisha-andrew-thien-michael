package bankapp;

import java.util.ArrayList;
import java.util.List;
import bankapp.Transaction;
import java.util.UUID;

public class BankAccount {

	private double balance;
	private List<Double> balanceHistory;
	private boolean isFrozen;
	private List<Transaction> transactions;
	private String username;
	private String password;
	private String accountName;
	private String accountType;
	private double monthlySpendingLimit = Double.MAX_VALUE;
	private double currentSpent = 0.0;
	private String accountNumber;

	public BankAccount(String accountType) {
		if (!"Checking".equalsIgnoreCase(accountType) && !"Savings".equalsIgnoreCase(accountType)) {
		    throw new IllegalArgumentException("Account type must be 'Checking' or 'Savings'.");
		}
		
		this.accountType = accountType;
		this.accountNumber = generateAccountNumber(accountType);
		this.username = "";
		this.password = "";
		this.balanceHistory = new ArrayList<>();
		this.balance = 0;
		this.isFrozen = false;
		this.accountName = "";
		this.transactions = new ArrayList<>();
		balanceHistory.add(this.balance);
	}
	
	public void deposit(double amount) {
		if(!this.isFrozen) {
			if(amount < 0) {
				throw new IllegalArgumentException();
			}
			this.balance += amount;
			balanceHistory.add(this.balance);
			this.transactions.add(new Transaction("deposit", amount));
            System.out.println("Deposited: " + amount);
		}
	}

	public void withdraw(double amount){
		if(!this.isFrozen) {
			if(amount < 0 || amount > this.balance){
		        throw new IllegalArgumentException();
		    }
	        if (this.currentSpent + amount > this.monthlySpendingLimit) {
	            throw new IllegalArgumentException("Withdrawal would exceed the monthly spending limit.");
	        }
		    this.balance -= amount;
	        this.currentSpent += amount;
		    balanceHistory.add(this.balance);
		    this.transactions.add(new Transaction("withdraw", amount));
            System.out.println("Withdrawn: " + amount);
		}
	}

	public void depositMultiplePeriods(double amount, int periods) {
		if (amount < 0 || periods <= 0) {
	        	throw new IllegalArgumentException();
	    	}
	    
	    	for (int i = 0; i < periods; i++) {
	        	this.deposit(amount);
	    	}
	        System.out.println("Deposited: " + amount + ", Periods: " + periods);
	}

	public void withdrawMultiplePeriods(double amount, int periods) {
	    	if (amount < 0 || periods <= 0 || amount * periods > this.balance) {
	        	throw new IllegalArgumentException();
	    	}
	    
	    	for (int i = 0; i < periods; i++) {
	        	this.withdraw(amount);
	    	}
	        System.out.println("Withdrawn: " + amount + ", Periods: " + periods);
	}
	
	public void freeze() {
		this.isFrozen = true;
	}
	
	public void unfreeze() {
		this.isFrozen = false;
	}
	
	public double getCurrentBalance() {
		return this.balance;
	}
	
	public void setUsername(String u) {
		this.username = u;
	}
	
	public void setPassword(String p) {
		this.password = p;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public boolean validatePassword(String inputPassword) {
		return inputPassword != null && inputPassword.equals(this.password);
	}

    public List<Double> getBalanceHistory() {
		return new ArrayList<>(balanceHistory);
    }
	
	public boolean getFrozenStatus() {
		return this.isFrozen;
	}

	public void setAccountName(String name) {
		this.accountName = name;
	}

	public String getAccountName() {
		return this.accountName;
  }
	
	public List<Transaction> getTransactions() {
	    return new ArrayList<>(transactions);
	}
	
	public String getAccountType() {
	    return accountType;
	}
	
	public void setMonthlySpendingLimit(double limit) {
	    if (limit < 0) {
	        throw new IllegalArgumentException("Spending limit must be non-negative.");
	    }
	    this.monthlySpendingLimit = limit;
	}

	public double getMonthlySpendingLimit() {
	    return this.monthlySpendingLimit;
	}

	public double getCurrentSpent() {
	    return this.currentSpent;
	}

	public void resetMonthlySpending() {
	    this.currentSpent = 0;
	}

	private String generateAccountNumber(String type) {
	    String prefix = type.equalsIgnoreCase("Checking") ? "CHK" : "SVG";
	    return prefix + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	}
	
	public String getAccountNumber() {
	    return accountNumber;
	}
}
