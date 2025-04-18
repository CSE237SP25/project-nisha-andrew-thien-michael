package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bankapp.BankAccount;
import bankapp.Menu;
import bankapp.Transaction;

public class MenuTests {
	
	private final PrintStream originalOut = System.out;
	private final InputStream originalIn = System.in;
	private ByteArrayOutputStream testOut;
	private Menu testMenuInstance;
	
	
	private void setMenuScanner(Menu menuInstance, InputStream inputStream) throws Exception {
		Field scannerField = Menu.class.getDeclaredField("scanner");
		scannerField.setAccessible(true);
		scannerField.set(menuInstance, new Scanner(inputStream));
	}
	
	private void setLoggedInAccount(Menu menuInstance, BankAccount account) throws Exception {
		Field loggedInField = Menu.class.getDeclaredField("loggedInAccount");
		loggedInField.setAccessible(true);
		loggedInField.set(menuInstance, account);
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String, BankAccount> getAccountsMap(Menu menuInstance) throws Exception {
		Field accountsField = Menu.class.getDeclaredField("accounts");
		accountsField.setAccessible(true);
		return (HashMap<String, BankAccount>) accountsField.get(menuInstance);
	}
	
	@BeforeEach
	public void setupTest() {
		testMenuInstance = new Menu();
		testOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOut));
	}
	
	@AfterEach
	public void restoreSystemStreams() {
		System.setOut(originalOut);
		System.setIn(originalIn);
		try {
			Field scannerField = Menu.class.getDeclaredField("scanner");
			scannerField.setAccessible(true);
			Scanner instanceScanner = (Scanner) scannerField.get(testMenuInstance);
		}
		catch (Exception e) {
			
		}
	}

    @Test
    public void testBalanceHistoryOptionDisplaysCorrectly() {
        String simulatedInput = "4" + System.lineSeparator() + "5" + System.lineSeparator();

        BankAccount account = new BankAccount("Checking");
        account.setUsername("testUser");

        try {
            account.deposit(50.0);
            account.withdraw(20.0);
        } catch (IllegalArgumentException e) {
            fail("Test setup failed: direct deposit/withdraw: " + e.getMessage());
        }
        assertEquals(3, account.getBalanceHistory().size(), "Pre-condition: Account history size incorrect.");
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
   
        
        try {
            setLoggedInAccount(testMenuInstance, account);
            setMenuScanner(testMenuInstance, testIn);
            
            testMenuInstance.showMainMenu();

            String output = testOut.toString().replace("\r\n", "\n");

            assertTrue(output.contains("Balance History:"), "Output missing 'Balance History:' title.");
            assertTrue(output.contains("Step 0: $0.0"), "Output missing history step 0.");
            assertTrue(output.contains("Step 1: $50.0"), "Output missing history step 1.");
            assertTrue(output.contains("Step 2: $30.0"), "Output missing history step 2.");

        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage(), e);
        } 
    }
    
    
    @Test
    public void testTransactionCountOptionDisplaysZero() {
    	String simulatedInput = "8" + System.lineSeparator() + "5" + System.lineSeparator();
    	
    	BankAccount account = new BankAccount("Checking");
    	account.setUsername("zeroTxUser");
    	
    	assertEquals(0, account.getTransactions().size(), "Pre-condition failed: Should be 0 transactions.");
    	
    	ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
    	
    	try {
    		setLoggedInAccount(testMenuInstance, account);
    		setMenuScanner(testMenuInstance, testIn);
    		
    		testMenuInstance.showMainMenu();
    		
    		String output = testOut.toString().replace("\r\n", "\n");
    		
    		assertTrue(output.contains("Total transactions performed: 0"), "Output should show 0 transactions. Output was: " + output);
    		
    	}
    	catch (Exception e) {
    		fail("Test execution failed: " + e.getMessage(), e);
    	}
    }
    
    @Test
    public void testSortedTransactionsEmpty() {
    	String simulatedInput = "15" + System.lineSeparator() + "5" + System.lineSeparator();
    	
    	BankAccount account = new BankAccount("Savings");
    	account.setUsername("emptySortUser");
    	
    	assertEquals(0, account.getTransactions().size(), "Pre-condition failed: Should be 0 transactions.");
    	
    	ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
    	
    	try {
    		setLoggedInAccount(testMenuInstance, account);
    		setMenuScanner(testMenuInstance, testIn);
    		testMenuInstance.showMainMenu();
    		String output = testOut.toString().replace("\r\n", "\n");
    		
    		assertTrue(output.contains("Transaction History (Sorted by Highest Amount)"), "Sorted history title missing.");
    		assertTrue(output.contains("No transactions yet."), "Output should indicate no transactions. Output was: " + output);
    	}
    	catch(Exception e) {
    		fail("Test execution failed: " + e.getMessage(), e);
    	}
    			
    }
    
    @Test
    public void testSortedTransactionsMultiple() {
    	String simulatedInput = "15" + System.lineSeparator() + "5" + System.lineSeparator();
    	
    	BankAccount account = new BankAccount("Checking");
    	account.setUsername("multiSortUser");
    	
    	try {
    		account.deposit(50.55);
    		account.withdraw(10.10);
    		account.deposit(150.75);
    		account.withdraw(5.00);
    		account.deposit(10.10);
    	}
    	catch (IllegalArgumentException e) {
    		fail("Test setup failed during direct deposit/withdraw: " + e.getMessage());
    	}
    	
    	assertEquals(5, account.getTransactions().size(), "Pre-condition failed: Should be 5 transactions.");
    	
    	ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
    	
    	try {
    		setLoggedInAccount(testMenuInstance, account);
    		setMenuScanner(testMenuInstance, testIn);
    		testMenuInstance.showMainMenu();
    		String output = testOut.toString().replace("\r\n", "\n");
    		
    		assertTrue(output.contains("Transaction History (Sorted by Highest Amount)"), "Sorted history title missing.");
    		String expectedOrderAmounts[] = {"150.75", "50.55", "10.10", "10.10", "5.00"};
    		Pattern transactionLinePattern = Pattern.compile("(?m)^\\w+:\\s*\\$([0-9]+\\.[0-9]+)\\s+at\\s+.+");
    		Matcher matcher = transactionLinePattern.matcher(output);
    		int matchCount = 0;
    		int lastIndex = -1;
    		while(matcher.find()) {
    			String transactionLine = matcher.group(0);
    			String transactionAmount = matcher.group(1);
    			if (matchCount < expectedOrderAmounts.length) {
    				assertEquals(expectedOrderAmounts[matchCount], transactionAmount, "Transaction amount mismatch at sorted position " + matchCount + ". Line: " + transactionLine);
    			}
    			else {
    				fail("Found more transaction lines than expection (" + expectedOrderAmounts.length + "). Line: " + transactionLine);
    			}
    			int currentIndex = matcher.start();
    			assertTrue(currentIndex > lastIndex, "Transaction lines are not in the expected order in the output string. Check line: " + transactionLine);
    			lastIndex = currentIndex;
    			matchCount++;
    		}
    		assertEquals(expectedOrderAmounts.length, matchCount, "Did not find the expected number of transaction lines in the output.");
    	}
    	catch(Exception e) {
    		fail("Test execution failed: " + e.getMessage(), e);
    	}
    }
    
    @Test
    public void trestTransferFundsSuccess() throws Exception {
    	BankAccount sender = new BankAccount("Checking");
    	sender.setUsername("sender1");
    	sender.deposit(200.00);
    	BankAccount recipient = new BankAccount("Savings");
    	recipient.setUsername("receiver1");
    	
    	HashMap<String, BankAccount> accountMap = getAccountsMap(testMenuInstance);
    	accountMap.put(sender.getUsername(), sender);
    	accountMap.put(recipient.getUsername(), recipient);
    	setLoggedInAccount(testMenuInstance, sender);
    	
    	String simulatedInput = "16\nreceiver1\n75.50\n5\n";
    	ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
    	setMenuScanner(testMenuInstance, testIn);
    	
    	testMenuInstance.showMainMenu();
    	String output = testOut.toString().replace("\r\n", "\n");
    	assertTrue(output.contains("Successfully transferred $75.50 to user 'receiver1'."), "Sucess message missing or incorrect.");
    	
    }
    
    @Test
    public void testTransferFundsInsufficientFunds() throws Exception {
    	BankAccount sender = new BankAccount("Checking");
    	sender.setUsername("sender2");
    	sender.deposit(50.00);
    	BankAccount recipient = new BankAccount("Savings");
    	recipient.setUsername("receiver2");
    	
    	HashMap<String, BankAccount> accountMap = getAccountsMap(testMenuInstance);
    	accountMap.put(sender.getUsername(), sender);
    	accountMap.put(recipient.getUsername(), recipient);
    	setLoggedInAccount(testMenuInstance, sender);
    	
    	String simulatedInput = "16\nreceiver2\n75.00\n5\n";
    	ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
    	setMenuScanner(testMenuInstance, testIn);
    	
    	testMenuInstance.showMainMenu();
    	String output = testOut.toString().replace("\r\n", "\n");
    	assertTrue(output.contains("Transfer failed:"), "Failure indicator missing.");
    	assertTrue(output.toLowerCase().contains("exceeds balance") || output.toLowerCase().contains("spending limit"),
    	           "Error message should mention balance or spending limit issue.");
    	
    }
    
    @Test
    public void testTransferFundsRecipientNotFound() throws Exception {
    	BankAccount sender = new BankAccount("Checking");
    	sender.setUsername("sender3");
    	sender.deposit(100.00);
    	
    	HashMap<String, BankAccount> accountMap = getAccountsMap(testMenuInstance);
    	accountMap.put(sender.getUsername(), sender);
    	setLoggedInAccount(testMenuInstance, sender);
    	
    	String simulatedInput = "16\nnoOneHere\n50.00\n5\n";
    	ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
    	setMenuScanner(testMenuInstance, testIn);
    	
    	testMenuInstance.showMainMenu();
    	String output = testOut.toString().replace("\r\n", "\n");
    	assertTrue(output.contains("Error: Recipient user 'noOneHere' not found."), "Recipient not found message missing.");
    }
    
    @Test
    public void testTransferFundsToSelf() throws Exception {
    	BankAccount sender = new BankAccount("Checking"); 
    	sender.setUsername("selfSender"); 
    	sender.deposit(100.00);
    	
    	HashMap<String, BankAccount> accountMap = getAccountsMap(testMenuInstance);
    	accountMap.put(sender.getUsername(), sender);
        setLoggedInAccount(testMenuInstance, sender);
        
        String simulatedInput = "16\nselfSender\n50.00\n5\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        setMenuScanner(testMenuInstance, testIn);
        
        testMenuInstance.showMainMenu();
        
        String output = testOut.toString().replace("\r\n", "\n");
        assertTrue(output.contains("Error: Cannot transfer funds to yourself."), "Self-transfer error message missing.");
    }
    
    @Test
    public void testTransferFundsNegativeAmount() throws Exception {
    	BankAccount sender = new BankAccount("Checking"); 
    	sender.setUsername("sender4"); 
    	sender.deposit(100.00);
        BankAccount recipient = new BankAccount("Savings"); 
        recipient.setUsername("receiver4");
        
        HashMap<String, BankAccount> accountMap = getAccountsMap(testMenuInstance);
        accountMap.put(sender.getUsername(), sender);
        accountMap.put(recipient.getUsername(), recipient);
        setLoggedInAccount(testMenuInstance, sender);
        
        String simulatedInput = "16\nreceiver4\n-50.00\n5\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        setMenuScanner(testMenuInstance, testIn);
        
        testMenuInstance.showMainMenu();
        String output = testOut.toString().replace("\r\n", "\n");
        assertTrue(output.contains("Error: Transfer amount must be positive."), "Negative amount error missing.");
    }
    
    @Test
    public void testTransferFundsSenderFrozen() throws Exception {
    	BankAccount sender = new BankAccount("Checking"); 
    	sender.setUsername("frozenSender"); 
    	sender.deposit(100.00); 
    	sender.freeze();
    	BankAccount recipient = new BankAccount("Savings"); 
    	recipient.setUsername("receiver5");
    	
    	HashMap<String, BankAccount> accountMap = getAccountsMap(testMenuInstance);
        accountMap.put(sender.getUsername(), sender);
        accountMap.put(recipient.getUsername(), recipient);
        setLoggedInAccount(testMenuInstance, sender);
        
        String simulatedInput = "16\nreceiver5\n50.00\n5\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        setMenuScanner(testMenuInstance, testIn);
        
        testMenuInstance.showMainMenu();
        
        String output = testOut.toString().replace("\r\n", "\n");
        assertTrue(output.contains("Error: Your account is currently frozen."), "Sender frozen error missing.");
    }
    
    @Test
    public void testTransferFundsRecipientFrozen() throws Exception {
    	BankAccount sender = new BankAccount("Checking"); 
    	sender.setUsername("sender6"); 
    	sender.deposit(100.00);
        BankAccount recipient = new BankAccount("Savings"); 
        recipient.setUsername("frozenReceiver"); 
        recipient.freeze();
        
        HashMap<String, BankAccount> accountMap = getAccountsMap(testMenuInstance);
        accountMap.put(sender.getUsername(), sender);
        accountMap.put(recipient.getUsername(), recipient);
        setLoggedInAccount(testMenuInstance, sender);
        
        String simulatedInput = "16\nfrozenReceiver\n50.00\n5\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        setMenuScanner(testMenuInstance, testIn);
        
        testMenuInstance.showMainMenu();
        String output = testOut.toString().replace("\r\n", "\n");
        assertTrue(output.contains("Error: Recipient's account is currently frozen."), "Recipient frozen error missing.");
    }
}