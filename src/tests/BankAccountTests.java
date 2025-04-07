package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import java.util.List;
import bankapp.Transaction;
import java.time.LocalDateTime;


import org.junit.jupiter.api.Test;

import bankapp.BankAccount;

public class BankAccountTests {

	@Test
	public void testSimpleDeposit() {
		//1. Create objects to be tested
		BankAccount account = new BankAccount();
		
		//2. Call the method being tested
		account.deposit(25);
		
		//3. Use assertions to verify results
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}
	
	@Test
	public void testNegativeDeposit() {
		//1. Create object to be tested
		BankAccount account = new BankAccount();

		try {
			account.deposit(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}

	@Test
	public void testSimpleWithdraw() {
		BankAccount account = new BankAccount();
		account.deposit(25);
		account.withdraw(10);
		assertEquals(account.getCurrentBalance(), 15.0, 0.005);
	}
	
	@Test
	public void testNegativeWithdraw() {
	    	BankAccount account = new BankAccount();
	  	account.deposit(25);
	    	try {
	        	account.withdraw(-25);
	        	fail();
	    	} catch (IllegalArgumentException e){
	        	assertTrue(e != null);
	    	}
	}
	
	@Test
	public void testIllegalWithdraw() {
	    	BankAccount account = new BankAccount();
	    	account.deposit(25);
	    	try {
	        	account.withdraw(50);
	        	fail();
	    	} catch (IllegalArgumentException e){
	        	assertTrue(e != null);
	    	}
	}

	@Test
    public void testBalanceHistoryTracking() {
        BankAccount account = new BankAccount();
        account.deposit(50);      // 0 -> 50
        account.withdraw(20);     // 50 -> 30
        account.deposit(10);      // 30 -> 40

        List<Double> history = account.getBalanceHistory();

        assertEquals(4, history.size()); // [0.0, 50.0, 30.0, 40.0]
        assertEquals(0.0, history.get(0), 0.001);
        assertEquals(50.0, history.get(1), 0.001);
        assertEquals(30.0, history.get(2), 0.001);
        assertEquals(40.0, history.get(3), 0.001);
    }
	public void testNotFrozenStart() {
	    BankAccount account = new BankAccount();
	    assertFalse(account.getFrozenStatus());
	}
	
	@Test
	public void testFreeze(){
	    BankAccount account = new BankAccount();
	    account.freeze();
	    assertTrue(account.getFrozenStatus());
	}

	@Test
	public void testUnfreeze(){
	    BankAccount account = new BankAccount();
	    account.freeze();
	    assertTrue(account.getFrozenStatus());
	    account.unfreeze();
	    assertFalse(account.getFrozenStatus());
	}
	
	@Test
	public void testFrozenDeposit() {
		BankAccount account = new BankAccount();
		account.deposit(25);
		account.freeze();
		account.deposit(20);
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}

	@Test
	public void testFrozenWithdraw() {
		BankAccount account = new BankAccount();
		account.deposit(25);
		account.freeze();
		account.withdraw(20);
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}
	
	@Test
	public void testSetUsername() {
		BankAccount account = new BankAccount();
		account.setUsername("andrew");
		assertEquals(account.getUsername(), "andrew");
	}
	
	@Test
	public void testSetPassword() {
		BankAccount account = new BankAccount();
		account.setPassword("hello");
		assertEquals(account.getPassword(), "hello");
	}
	
	@Test
	public void testCorrectPasswordValidation() {
		BankAccount account = new BankAccount();
		account.setPassword("hello");
		assertTrue(account.validatePassword("hello"));
	}
	
	@Test
	public void testIncorrectPasswordValidation() {
		BankAccount account = new BankAccount();
		account.setPassword("hello");
		assertFalse(account.validatePassword("hi"));
	}
	

	@Test
	public void testNullPasswordValidation() {
		BankAccount account = new BankAccount();
		account.setPassword(null);
		assertFalse(account.validatePassword(null));
	}
	
	@Test
	public void testTransactionTracking() {
	    BankAccount account = new BankAccount();

	    account.deposit(150.00);
	    account.withdraw(50.00);

	    List<Transaction> transactions = account.getTransactions();

	    assertEquals(2, transactions.size());

	    Transaction first = transactions.get(0);
	    Transaction second = transactions.get(1);

	    assertTrue(first.toString().contains("deposit"));
	    assertTrue(first.toString().contains("150.0"));

	    assertTrue(second.toString().contains("withdraw"));
	    assertTrue(second.toString().contains("50.0"));
	}

	
}
