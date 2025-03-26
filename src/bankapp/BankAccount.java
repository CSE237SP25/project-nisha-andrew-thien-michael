package bankapp;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

	private double balance;
	private List<Double> balanceHistory;
	
	public BankAccount() {t
		this.balance = 0;
	}
	
	public void deposit(double amount) {
		if(amount < 0) {
			throw new IllegalArgumentException();
		}
		this.balance += amount;
	}

	public void withdraw(double amount){
	        if(amount < 0 || amount > this.balance()){
	        	throw new IllegalArgumentException();
	        }
	        this.balance -= amount;        
	}
	
	public double getCurrentBalance() {
		return this.balance;
	}

    public List<Double> getBalanceHistory() {
        return new ArrayList<>(balanceHistory);
    }
}
