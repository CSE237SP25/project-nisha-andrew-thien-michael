package bankapp;

public class BankAccount {

	private double balance;
    	private boolean isFrozen;
	
	public BankAccount() {
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

	public void freeze(){
	        this.isFrozen = True;
	}
	
	public void unfreeze(){
	        this.isFrozen = False;
	}
	
    	public boolean getFrozenStatus(){
        	return this.isFrozen;
    	}
}
