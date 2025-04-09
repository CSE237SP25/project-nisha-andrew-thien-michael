package bankapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankAccount {

	private double balance;
	private List<Double> balanceHistory;
	private boolean isFrozen;
	
	private String username;
	private String password;

    
    private double monthlyLimit = Double.MAX_VALUE;
    private double currentSpent = 0.0;
	
	public BankAccount(String accountType) {
        if (!"Checking".equalsIgnoreCase(accountType) && !"Savings".equalsIgnoreCase(accountType)) {
            throw new IllegalArgumentException("Account type must be 'Checking' or 'Savings'.");
        }

		this.username = "";
		this.password = "";
		this.balanceHistory = new ArrayList<>();
		this.balance = 0;
		this.isFrozen = false;
		balanceHistory.add(this.balance);
	}
	
	public void deposit(double amount) {
		if(!this.isFrozen) {
			if(amount < 0) {
				throw new IllegalArgumentException();
			}
			this.balance += amount;
			balanceHistory.add(this.balance);
		}
		
	}

	public void withdraw(double amount){
		if(this.isFrozen) return;
			
		if (amount <= 0) {
			throw new IllegalArgumentException("Withdraw amount must be positive.");
		}
		if (amount > this.balance) {
			throw new IllegalArgumentException("Insufficient funds. Amount is greater than balance.");
		}
		
        if ((this.currentSpent + amount) > this.monthlyLimit) {
            throw new IllegalArgumentException("You are exceeding the monthly spending limit!");
        }
		    this.balance -= amount;
	        this.currentSpent += amount;
		    balanceHistory.add(this.balance);
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
    
    public void setMonthlyLimit(double limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit can't be negative.");
        }
        this.monthlyLimit = limit;
    }
    
    public double getMonthlyLimit() {
        return this.monthlyLimit;
    }

    public double getCurrentSpent() {
        return this.currentSpent;
    }

    public void resetMonthlySpent() {
        this.currentSpent = 0.0;
    }
}

