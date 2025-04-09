package bankapp;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

	private double balance;
	private List<Double> balanceHistory;
	private boolean isFrozen;
	
	private String username;
	private String password;
	
	public BankAccount() {
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
			this.transactions.add(new Transaction("deposit", amount));
            System.out.println("Deposited: " + amount);
		}
	}

	public void withdraw(double amount){
		if(!this.isFrozen) {
			if(amount < 0 || amount > this.balance){
		        throw new IllegalArgumentException();
		    }
		    this.balance -= amount;
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
		}
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
}
