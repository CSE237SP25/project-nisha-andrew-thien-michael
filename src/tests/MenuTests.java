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

import org.junit.jupiter.api.Test;

import bankapp.BankAccount;
import bankapp.Menu;

public class MenuTests {

    @Test
    public void testBalanceHistoryOptionDisplaysCorrectly() {
        String simulatedInput = "4" + System.lineSeparator() + "5" + System.lineSeparator();

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());

        Menu testMenuInstance = new Menu();
        BankAccount account = new BankAccount();
        account.setUsername("testUser");

        try {
            account.deposit(50.0);
            account.withdraw(20.0);
        } catch (IllegalArgumentException e) {
            fail("Test setup failed: direct deposit/withdraw: " + e.getMessage());
        }
        assertEquals(3, account.getBalanceHistory().size(), "Pre-condition: Account history size incorrect.");

        try {
            System.setOut(new PrintStream(testOut));

            Field loggedInField = Menu.class.getDeclaredField("loggedInAccount");
            loggedInField.setAccessible(true);
            loggedInField.set(testMenuInstance, account);

            Field scannerField = Menu.class.getDeclaredField("scanner");
            scannerField.setAccessible(true);
            scannerField.set(testMenuInstance, new Scanner(testIn));

            testMenuInstance.showMainMenu();

            String output = testOut.toString().replace("\r\n", "\n");

            assertTrue(output.contains("Balance History:"), "Output missing 'Balance History:' title.");
            assertTrue(output.contains("Step 0: $0.0"), "Output missing history step 0.");
            assertTrue(output.contains("Step 1: $50.0"), "Output missing history step 1.");
            assertTrue(output.contains("Step 2: $30.0"), "Output missing history step 2.");

        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage(), e);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }
}