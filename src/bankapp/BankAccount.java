package bankapp;

public class BankAccount {

	private double balance;
	private boolean isFrozen;
	
	public BankAccount() {
		this.balance = 0;
		this.isFrozen = false;
	}
	
	public void deposit(double amount) {
		if(!this.isFrozen) {
			if(amount < 0) {
				throw new IllegalArgumentException();
			}
			this.balance += amount;
		}
	}

	public void withdraw(double amount){
		if(!this.isFrozen) {
			if(amount < 0 || amount > this.balance){
		        throw new IllegalArgumentException();
		    }
		    this.balance -= amount;
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
	
	public boolean getFrozenStatus() {
		return this.isFrozen;
	}
}
