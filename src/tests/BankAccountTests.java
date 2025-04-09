package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import java.util.List;

import org.junit.jupiter.api.Test;

import bankapp.BankAccount;

public class BankAccountTests {

	@Test
	public void testSimpleDeposit() {
		//1. Create objects to be tested
		BankAccount account = new BankAccount("Checking");
		
		//2. Call the method being tested
		account.deposit(25);
		
		//3. Use assertions to verify results
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}
	
	@Test
	public void testNegativeDeposit() {
		//1. Create object to be tested
		BankAccount account = new BankAccount("Checking");

		try {
			account.deposit(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}

	@Test
	public void testSimpleWithdraw() {
		BankAccount account = new BankAccount("Checking");
		account.deposit(25);
		account.withdraw(10);
		assertEquals(account.getCurrentBalance(), 15.0, 0.005);
	}
	
	@Test
	public void testNegativeWithdraw() {
	    	BankAccount account = new BankAccount("Checking");
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
	    	BankAccount account = new BankAccount("Checking");
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
        BankAccount account = new BankAccount("Checking");
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
	    BankAccount account = new BankAccount("Checking");
	    assertFalse(account.getFrozenStatus());
	}
	
	@Test
	public void testFreeze(){
	    BankAccount account = new BankAccount("Checking");
	    account.freeze();
	    assertTrue(account.getFrozenStatus());
	}

	@Test
	public void testUnfreeze(){
	    BankAccount account = new BankAccount("Checking");
	    account.freeze();
	    assertTrue(account.getFrozenStatus());
	    account.unfreeze();
	    assertFalse(account.getFrozenStatus());
	}
	
	@Test
	public void testFrozenDeposit() {
		BankAccount account = new BankAccount("Checking");
		account.deposit(25);
		account.freeze();
		account.deposit(20);
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}

	@Test
	public void testFrozenWithdraw() {
		BankAccount account = new BankAccount("Checking");
		account.deposit(25);
		account.freeze();
		account.withdraw(20);
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}
	
	@Test
	public void testSetUsername() {
		BankAccount account = new BankAccount("Checking");
		account.setUsername("andrew");
		assertEquals(account.getUsername(), "andrew");
	}
	
	@Test
	public void testSetPassword() {
		BankAccount account = new BankAccount("Checking");
		account.setPassword("hello");
		assertEquals(account.getPassword(), "hello");
	}
	
	@Test
	public void testCorrectPasswordValidation() {
		BankAccount account = new BankAccount("Checking");
		account.setPassword("hello");
		assertTrue(account.validatePassword("hello"));
	}
	
	@Test
	public void testIncorrectPasswordValidation() {
		BankAccount account = new BankAccount("Checking");
		account.setPassword("hello");
		assertFalse(account.validatePassword("hi"));
	}
	

	@Test
	public void testNullPasswordValidation() {
		BankAccount account = new BankAccount("Checking");
		account.setPassword(null);
		assertFalse(account.validatePassword(null));
	}
	
	@Test
	public void testAccountTypeAndNumber() {
	    BankAccount checkingAccount = new BankAccount("Checking");
	    assertEquals("Checking", checkingAccount.getAccountType());
	    assertTrue(checkingAccount.getAccountNumber().startsWith("CHK-"));

	    BankAccount savingsAccount = new BankAccount("Savings");
	    assertEquals("Savings", savingsAccount.getAccountType());
	    assertTrue(savingsAccount.getAccountNumber().startsWith("SVG-"));
	}

	@Test
	public void testInvalidAccountTypeThrowsException() {
	    try {
	        new BankAccount("Business");
	        fail("Should throw exception for invalid account type");
	    } catch (IllegalArgumentException e) {
	        assertEquals("Account type must be 'Checking' or 'Savings'.", e.getMessage());
	    }
	}
}
