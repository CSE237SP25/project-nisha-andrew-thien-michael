package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import java.util.List;
import bankapp.Transaction;
import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


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
    public void testDepositReceiptOutput() {
        BankAccount account = new BankAccount("Checking");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        account.deposit(100);
        String output = outContent.toString().trim();

        assertTrue(output.contains("Deposited: 100.0"));
        System.setOut(System.out);
    }

    @Test
    public void testWithdrawReceiptOutput() {
        BankAccount account = new BankAccount("Checking");
        account.deposit(200);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        account.withdraw(50);
        String output = outContent.toString().trim();

        assertTrue(output.contains("Withdrawn: 50.0"));
        System.setOut(System.out);
    }

	@Test
	public void testMultipleDeposit() {
		BankAccount account = new BankAccount("Checking");
		account.depositMultiplePeriods(5, 3);
		assertEquals(account.getCurrentBalance(), 15.0, 0.005);
	}
	
	@Test
	public void testMultipleNegativeDeposit() {
		BankAccount account = new BankAccount("Checking");
    	try {
        	account.depositMultiplePeriods(-5, 3);
        	fail();
    	} catch (IllegalArgumentException e){
        	assertTrue(e != null);
    	}
	}
	
	@Test
	public void testMultipleWithdraw() {
		BankAccount account = new BankAccount("Checking");
		account.deposit(25);
		account.withdrawMultiplePeriods(5, 3);
		assertEquals(account.getCurrentBalance(), 10.0, 0.005);
	}

	@Test
	public void testMultipleNegativeWithdraw() {
		BankAccount account = new BankAccount("Checking");
    		try {
        		account.withdrawMultiplePeriods(-5, 3);
        		fail();
    		} catch (IllegalArgumentException e){
        		assertTrue(e != null);
    		}
	}
	

	@Test
	public void testMultipleIllegalWithdraw() {
		BankAccount account = new BankAccount("Checking");
		account.deposit(10);
    		try {
        		account.withdrawMultiplePeriods(5, 3);
        		fail();
    		} catch (IllegalArgumentException e){
        		assertTrue(e != null);
    		}
	}
	

    @Test
    public void testMultipleDepositReceiptOutput() {
        BankAccount account = new BankAccount("Checking");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        account.depositMultiplePeriods(50, 3);
        String output = outContent.toString().trim();
        
        assertTrue(output.contains("Deposited: 50.0, Periods: 3"));
        System.setOut(System.out);
    }

    @Test
    public void testMultipleWithdrawReceiptOutput() {
        BankAccount account = new BankAccount("Checking");
        account.deposit(200);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        account.withdrawMultiplePeriods(50, 3);
        String output = outContent.toString().trim();

        assertTrue(output.contains("Withdrawn: 50.0, Periods: 3"));
        System.setOut(System.out);
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
	
	@Test
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
	public void testSetAndGetAccountName() {
		BankAccount account = new BankAccount("Checking");
		account.setAccountName("Vacation Fund");
		assertEquals("Vacation Fund", account.getAccountName());
	}

	
	@Test
	public void testTransactionTracking() {
	    BankAccount account = new BankAccount("Checking");

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
	
	@Test
	void testValidAccountTypes() {
	    BankAccount checking = new BankAccount("Checking");
	    BankAccount savings = new BankAccount("Savings");

	    assertEquals("Checking", checking.getAccountType());
	    assertEquals("Savings", savings.getAccountType());
	}

	@Test
	void testInvalidAccountTypeThrowsException() {
	    try {
	        new BankAccount("Investment");
	        fail("Should throw exception for valid account type");
	    }
	    catch(IllegalArgumentException e){
	    assertEquals("Account type must be 'Checking' or 'Savings'.", e.getMessage());
	}
	}
}
