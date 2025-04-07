package bankapp;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

	private double balance;
	private List<Double> balanceHistory;
	private boolean isFrozen;
	
	private String username;
	private String password;
	private String accountName;


	
	public BankAccount() {
		this.username = "";
		this.password = "";
		this.balanceHistory = new ArrayList<>();
		this.balance = 0;
		this.isFrozen = false;
		this.accountName = "";
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
		if(!this.isFrozen) {
			if(amount < 0 || amount > this.balance){
		        throw new IllegalArgumentException();
		    }
		    this.balance -= amount;
		    balanceHistory.add(this.balance);
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

	public void setAccountName(String name) {
		this.accountName = name;
	}

	public String getAccountName() {
		return this.accountName;
	}

	
}
