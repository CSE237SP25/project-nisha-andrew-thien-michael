package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bankapp.BankAccount;
import bankapp.Menu;

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

        BankAccount account = new BankAccount();
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
    	
    	BankAccount account = new BankAccount();
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
}