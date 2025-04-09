package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

import bankapp.BankAccount;
import bankapp.Menu;

public class MenuLoginTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private ByteArrayOutputStream testOut;
    private Menu testMenuInstance;

    @SuppressWarnings("unchecked")
    private HashMap<String, BankAccount> getAccountsMap(Menu menuInstance) throws Exception {
        Field accountsField = Menu.class.getDeclaredField("accounts");
        accountsField.setAccessible(true);
        return (HashMap<String, BankAccount>) accountsField.get(menuInstance);
    }

    private void setMenuScanner(Menu menuInstance, InputStream inputStream) throws Exception {
        Field scannerField = Menu.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(menuInstance, new Scanner(inputStream));
    }

    private BankAccount getLoggedInAccount(Menu menuInstance) throws Exception {
        Field loggedInField = Menu.class.getDeclaredField("loggedInAccount");
        loggedInField.setAccessible(true);
        return (BankAccount) loggedInField.get(menuInstance);
    }

    @BeforeEach
    public void setupTest() throws Exception {
        testMenuInstance = new Menu();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void restoreSystemStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private String runInstanceMenuMethodWithInput(String inputData, String methodName, Class<?>... parameterTypes) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputData.getBytes());
        try {
            setMenuScanner(testMenuInstance, testIn);
            Method method = Menu.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(testMenuInstance);
        } catch (Exception e) {
            e.printStackTrace();
            return "TEST FAILED: EXCEPTION THROWN - " + e.getMessage();
        }
        return testOut.toString().replace("\r\n", "\n");
    }

    @Test
    void testCreateAccount_Success() throws Exception {
        String input = "testUser\npassword123\n";
        String output = runInstanceMenuMethodWithInput(input, "createAccount");

        assertTrue(output.contains("Account created successfully!"));
        HashMap<String, BankAccount> accounts = getAccountsMap(testMenuInstance);
        assertTrue(accounts.containsKey("testUser"));
        assertNotNull(accounts.get("testUser"));
        assertEquals("testUser", accounts.get("testUser").getUsername());
        assertTrue(accounts.get("testUser").validatePassword("password123"));
    }

    @Test
    void testCreateAccount_DuplicateUsername() throws Exception {
        String input1 = "testUser\npassword123\n";
        runInstanceMenuMethodWithInput(input1, "createAccount");

        String input2 = "testUser\notherpass\n";
        String output2 = runInstanceMenuMethodWithInput(input2, "createAccount");

        assertTrue(output2.contains("Username already exists."));
        assertEquals(1, getAccountsMap(testMenuInstance).size());
    }

     @Test
    void testCreateAccount_EmptyUsername() throws Exception {
        String input = "\n";
        String output = runInstanceMenuMethodWithInput(input, "createAccount");

        assertTrue(output.contains("Username cannot be empty."));
        assertFalse(output.contains("Account created successfully!"));
        assertTrue(getAccountsMap(testMenuInstance).isEmpty());
    }

     @Test
    void testCreateAccount_EmptyPassword() throws Exception {
        String input = "testUser\n\n";
        String output = runInstanceMenuMethodWithInput(input, "createAccount");

        assertTrue(output.contains("Password cannot be empty."));
        assertFalse(output.contains("Account created successfully!"));
        assertTrue(getAccountsMap(testMenuInstance).isEmpty());
    }
  
     @Test
     void testLogin_Success() throws Exception {
         String createInput = "loginUser\nloginPass\n";
         runInstanceMenuMethodWithInput(createInput, "createAccount");

         String loginInput = "1\nloginUser\nloginPass\n";
         String loginOutput = runInstanceMenuMethodWithInput(loginInput, "login");

         assertTrue(loginOutput.contains("Login successful! Welcome loginUser"));
         assertNotNull(getLoggedInAccount(testMenuInstance));
         assertEquals("loginUser", getLoggedInAccount(testMenuInstance).getUsername());
     }

    @Test
    void testLogin_Success() throws Exception {
    	String createInput = "loginUser\nloginPass\n";
    	runStaticMenuMethodWithInput(createInput, "createAccount");

    	String loginInput = "1\nloginUser\nloginPass\n\n";
    	String loginOutput = runStaticMenuMethodWithInput(loginInput, "login");

    	 assertTrue(loginOutput.contains("Login successful!"), "Output should confirm successful login.");
    }

    @Test
    void testLogin_WrongPassword() throws Exception {
        String createInput = "loginUser\nloginPass\n";
        runInstanceMenuMethodWithInput(createInput, "createAccount");

        String loginInput = "1\nloginUser\nwrongPass\n3\n";
        String loginOutput = runInstanceMenuMethodWithInput(loginInput, "login");

        assertTrue(loginOutput.contains("Invalid login credentials."));
        assertFalse(loginOutput.contains("Login successful!"));
        assertNull(getLoggedInAccount(testMenuInstance));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        String loginInput = "1\nnonExistentUser\nanyPass\n3\n";
        String loginOutput = runInstanceMenuMethodWithInput(loginInput, "login");

        assertTrue(loginOutput.contains("Invalid login credentials."));
        assertFalse(loginOutput.contains("Login successful!"));
        assertNull(getLoggedInAccount(testMenuInstance));
    }

    @Test
    void testForgotPassword_Success() throws Exception {
        String createInput = "resetUser\noldPass\n";
        runInstanceMenuMethodWithInput(createInput, "createAccount");

        String forgotInput = "2\nresetUser\nnewPass\nnewPass\n3\n";
        String forgotOutput = runInstanceMenuMethodWithInput(forgotInput, "login");

        assertTrue(forgotOutput.contains("Password updated successfully!"));

        HashMap<String, BankAccount> accounts = getAccountsMap(testMenuInstance);
        assertTrue(accounts.containsKey("resetUser"));
        assertTrue(accounts.get("resetUser").validatePassword("newPass"));
        assertFalse(accounts.get("resetUser").validatePassword("oldPass"));
        assertNull(getLoggedInAccount(testMenuInstance));
    }

    @Test
    void testForgotPassword_PasswordMismatch() throws Exception {
        String createInput = "resetUser\noldPass\n";
        runInstanceMenuMethodWithInput(createInput, "createAccount");

        String forgotInput = "2\nresetUser\nnewPass1\nnewPass2\n3\n";
        String forgotOutput = runInstanceMenuMethodWithInput(forgotInput, "login");

        assertTrue(forgotOutput.contains("Passwords do not match."));

        HashMap<String, BankAccount> accounts = getAccountsMap(testMenuInstance);
        assertTrue(accounts.get("resetUser").validatePassword("oldPass"));
        assertFalse(accounts.get("resetUser").validatePassword("newPass1"));
        assertNull(getLoggedInAccount(testMenuInstance));
    }

     @Test
    void testForgotPassword_EmptyNewPassword() throws Exception {
        String createInput = "resetUser\noldPass\n";
        runInstanceMenuMethodWithInput(createInput, "createAccount");

        String forgotInput = "2\nresetUser\n\n\n3\n";
        String forgotOutput = runInstanceMenuMethodWithInput(forgotInput, "login");

        assertTrue(forgotOutput.contains("Password cannot be empty."));
        assertFalse(forgotOutput.contains("Password updated successfully!"));

        HashMap<String, BankAccount> accounts = getAccountsMap(testMenuInstance);
        assertTrue(accounts.get("resetUser").validatePassword("oldPass"));
        assertNull(getLoggedInAccount(testMenuInstance));
    }

    @Test
    void testForgotPassword_UserNotFound() throws Exception {
        String forgotInput = "2\nnonExistentUser\nnewPass\nnewPass\n3\n";
        String forgotOutput = runInstanceMenuMethodWithInput(forgotInput, "login");

        assertTrue(forgotOutput.contains("Username does not exist."));
        assertFalse(forgotOutput.contains("Password updated successfully!"));
        assertNull(getLoggedInAccount(testMenuInstance));
    }
}